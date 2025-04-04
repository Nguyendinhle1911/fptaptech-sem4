import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CurrencyPipe, NgIf } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

declare var paypal: any;

@Component({
  selector: 'app-payment',
  imports: [CurrencyPipe, NgIf],
  templateUrl: './payment.component.html',
  standalone: true,
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  orderId: number | undefined;
  amount: number | undefined;
  loading: boolean = true;
  errorMessage: string = '';
  private paymentApiUrl = 'http://localhost:8080/api/payments/paypal/process';
  private updateStatusApiUrl = 'http://localhost:8080/api/payments/paypal/update-status';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('orderId'));
    this.amount = history.state.amount;
    if (!this.orderId || !this.amount) {
      this.errorMessage = 'Invalid order or amount!';
      this.loading = false;
      return;
    }
    this.loadPaypalButton();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  processPaypalPayment(orderId: number, amount: number): Observable<string> {
    return this.http.post(
      this.paymentApiUrl,
      { orderId, amount },
      { headers: this.getHeaders(), responseType: 'text' }
    ).pipe(
      map(response => {
        console.log('PayPal Order ID from API:', response);
        return response;
      }),
      catchError(error => {
        console.error('Payment API error:', error);
        return throwError(() => new Error(error.message || 'Payment failed'));
      })
    );
  }

  updatePaymentStatus(orderId: number, status: string, transactionId: string): Observable<void> {
    return this.http.post<void>(
      this.updateStatusApiUrl,
      { orderId, status, transactionId },
      { headers: this.getHeaders() }
    ).pipe(catchError(error => {
      console.error('Update status error:', error);
      return throwError(() => new Error(error.message || 'Failed to update payment status'));
    }));
  }

  loadPaypalButton(): void {
    this.loading = true;
    console.log('Loading PayPal button...');

    paypal
      .Buttons({
        createOrder: (data: any, actions: any) => {
          console.log('Creating PayPal order with amount:', this.amount);
          return this.processPaypalPayment(this.orderId!, this.amount!)
            .toPromise()
            .then(paypalOrderId => {
              if (!paypalOrderId) throw new Error('Failed to create PayPal order');
              console.log('PayPal Order ID:', paypalOrderId);
              return paypalOrderId;
            })
            .catch(err => {
              console.error('Error in createOrder:', err);
              throw err;
            });
        },
        onApprove: (data: any, actions: any) => {
          console.log('Order approved:', data);
          return actions.order.capture().then((details: any) => {
            console.log('Payment captured:', details);
            return this.updatePaymentStatus(this.orderId!, 'COMPLETED', details.id)
              .toPromise()
              .then(() => {
                alert('Payment completed successfully!');
                this.router.navigate(['/orders']);
              });
          });
        },
        onError: (err: any) => {
          console.error('PayPal error:', err);
          this.errorMessage = 'Payment failed: ' + err.message;
          this.loading = false;
        },
        onCancel: (data: any) => {
          console.log('Payment cancelled:', data);
          this.errorMessage = 'Payment was cancelled';
          this.loading = false;
        }
      })
      .render('#paypal-button-container')
      .then(() => {
        console.log('PayPal button rendered successfully');
        this.loading = false;
      })
      .catch((err: any) => {
        console.error('Failed to render PayPal button:', err);
        this.errorMessage = 'Failed to load PayPal button: ' + err;
        this.loading = false;
      });
  }

  goBack(): void {
    this.router.navigate(['/orders']);
  }
}
