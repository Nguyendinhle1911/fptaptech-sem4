// src/app/services/product.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products'; // URL của backend

  constructor(private http: HttpClient) { }

  // Hàm tạo headers với token
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      console.warn('No token found in localStorage');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json' // Thêm Content-Type để đảm bảo gửi JSON
    });
  }

  // Lấy danh sách tất cả sản phẩm
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl, { headers: this.getHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  // Lấy sản phẩm theo ID
  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${productId}`, { headers: this.getHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  // Thêm sản phẩm mới
  addProduct(product: { price: number; name: string; description: string; stock: number }): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product, { headers: this.getHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  // Cập nhật sản phẩm
  updateProduct(productId: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${productId}`, product, { headers: this.getHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  // Xóa sản phẩm
  deleteProduct(productId: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${productId}`, { headers: this.getHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  // Kiểm tra tồn kho
  checkStock(productId: number, quantity: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${productId}/checkStock/${quantity}`, { headers: this.getHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  // Xử lý lỗi
  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }
}

// Định nghĩa interface cho Product
export interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  description?: string;
}
