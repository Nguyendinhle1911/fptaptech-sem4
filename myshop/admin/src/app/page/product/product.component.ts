// src/app/product/product.component.ts
import { Component, OnInit } from '@angular/core';
import { ProductService, Product } from './product.service';
import {RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';
import { trigger, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  imports: [RouterLink, CommonModule],
  styleUrls: ['./product.component.scss'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({opacity: 0, transform: 'translateY(10px)'}),
        animate('300ms ease-in', style({opacity: 1, transform: 'translateY(0)'})),
      ]),
    ]),
  ],
  standalone: true
})
export class ProductComponent implements OnInit {
  products: Product[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Error fetching products:', err);
      }
    });
  }

  deleteProduct(productId: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          this.products = this.products.filter(product => product.id !== productId); // Cập nhật danh sách ngay lập tức
          alert('Product deleted successfully');
        },
        error: (err) => {
          console.error('Error deleting product:', err);
          alert('Failed to delete product');
        }
      });
    }
  }
}
