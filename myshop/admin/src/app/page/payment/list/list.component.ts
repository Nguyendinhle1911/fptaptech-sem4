import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {CurrencyPipe, DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-list',
  imports: [
    DatePipe,
    NgForOf,
    NgClass,
    CurrencyPipe,
    NgIf,
    RouterLink
  ],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  payments: any[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';
  private apiUrl = 'http://localhost:8080/api/payments/get-all';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    const token = localStorage.getItem('token');

    if (!token) {
      this.errorMessage = 'Vui lòng đăng nhập để xem danh sách thanh toán';
      this.isLoading = false;
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>(this.apiUrl, { headers }).subscribe({
      next: (data) => {
        this.payments = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Lỗi khi tải danh sách thanh toán', error);
        this.errorMessage = 'Không thể tải danh sách thanh toán. Vui lòng thử lại sau.';
        this.isLoading = false;
      }
    });
  }

  getBadgeClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'pending': return 'badge badge-pending';
      case 'completed': return 'badge badge-completed';
      case 'failed': return 'badge badge-failed';
      default: return 'badge badge-secondary';
    }
  }

  getRowClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'pending': return 'table-warning';
      case 'completed': return 'table-success';
      case 'failed': return 'table-danger';
      default: return '';
    }
  }


}
