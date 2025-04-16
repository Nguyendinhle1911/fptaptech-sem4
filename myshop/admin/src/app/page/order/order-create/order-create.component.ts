import { Component, OnInit } from '@angular/core';
import { OrderService, Product } from '../order.service';
import { CurrencyPipe, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-create',
  imports: [CurrencyPipe, NgForOf, NgIf, FormsModule],
  templateUrl: './order-create.component.html',
  standalone: true,
  styleUrls: ['./order-create.component.scss']
})
export class OrderCreateComponent implements OnInit {
  products: Product[] = [];
  selectedProducts: { productId: number; quantity: number }[] = [];
  paymentMethod: string = 'CASH';
  errorMessage: string = '';
  address: string = '';
  description: string = '';
  loading: boolean = false;

  constructor(private orderService: OrderService, private router: Router) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.orderService.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load products: ' + error.message;
        this.loading = false;
      }
    });
  }

  addProduct(productId: number): void {
    const existing = this.selectedProducts.find(p => p.productId === productId);
    if (existing) {
      existing.quantity += 1;
    } else {
      this.selectedProducts.push({ productId, quantity: 1 });
    }
  }

  removeProduct(productId: number): void {
    this.selectedProducts = this.selectedProducts.filter(p => p.productId !== productId);
  }

  updateQuantity(productId: number, quantity: number): void {
    const product = this.selectedProducts.find(p => p.productId === productId);
    if (product && quantity > 0) {
      product.quantity = quantity;
    }
  }

  getProductPrice(productId: number): number {
    const product = this.products.find(p => p.id === productId);
    return product ? product.price : 0;
  }

  calculateTotalPrice(): number {
    return this.selectedProducts.reduce((total, item) => {
      const price = this.getProductPrice(item.productId);
      return total + price * item.quantity;
    }, 0);
  }

  createOrder(): void {
    if (this.selectedProducts.length === 0) {
      this.errorMessage = 'Please select at least one product!';
      return;
    }

    if (!this.address) {
      this.errorMessage = 'Please provide a delivery address!';
      return;
    }

    const productQuantities: { [key: number]: number } = {};
    this.selectedProducts.forEach(p => {
      productQuantities[p.productId] = p.quantity;
    });

    const orderRequest = {
      productQuantities,
      paymentMethod: this.paymentMethod,
      address: this.address,          // Add address to request
      description: this.description   // Add description to request
    };

    this.loading = true;
    this.orderService.createOrder(orderRequest).subscribe({
      next: (response) => {
        alert('Order created successfully! Order ID: ' + response.id);
        if (this.paymentMethod === 'PAYPAL') {
          const totalPrice = this.calculateTotalPrice();
          const roundedTotalPrice = Number(totalPrice.toFixed(2));
          this.router.navigate(['/payment', response.id], {
            state: { amount: roundedTotalPrice }
          });
        } else {
          this.router.navigate(['/orders']);
        }
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to create order: ' + error.message;
        this.loading = false;
      }
    });
  }
}
