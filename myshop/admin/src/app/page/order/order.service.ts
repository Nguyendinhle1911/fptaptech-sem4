import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

// Định nghĩa các interface (model) ngay trong file này
export interface Order {
  id?: number;              // Optional vì khi tạo mới, id chưa tồn tại
  userId: number;           // ID của người dùng đặt hàng
  totalPrice: number;       // Tổng giá của đơn hàng
  orderDate: string;        // Ngày đặt hàng (chuỗi ISO date từ backend)
  address: string;          // Required delivery address
  description: string;      // Order description
  status: string;           // Trạng thái đơn hàng (PENDING, COMPLETED, CANCELLED)
  paymentMethod: string;    // Payment method
  products: OrderProduct[]; // Danh sách sản phẩm trong đơn hàng
}

export interface OrderProduct {
  productId: number;        // ID của sản phẩm
  quantity: number;         // Số lượng sản phẩm trong đơn hàng
}

export interface Product {
  id: number;               // ID của sản phẩm
  name: string;             // Tên sản phẩm
  price: number;            // Giá sản phẩm
  stock: number;            // Số lượng tồn kho
  description?: string;     // Mô tả sản phẩm (optional)
}

// Define the order request interface
export interface OrderRequest {
  productQuantities: { [key: number]: number };
  paymentMethod: string;
  address: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/api/orders';
  private productApiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) { }

  // Hàm tạo headers với token
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      console.warn('No token found in localStorage');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // Lấy tất cả orders
  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/all`, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  // API chỉ dành cho ADMIN
  getOrderByIdForAdmin(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/admin/${orderId}`, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  // Cập nhật trạng thái order
  updateOrderStatusForAdmin(orderId: number, status: string): Observable<Order> {
    return this.http.put<Order>(
      `${this.apiUrl}/admin/${orderId}/status`,
      { status },
      { headers: this.getHeaders() }
    ).pipe(catchError(this.handleError));
  }

  // Lấy danh sách sản phẩm từ Product Service
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productApiUrl, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  // Tạo đơn hàng mới - Updated with new interface
  createOrder(orderRequest: OrderRequest): Observable<Order> {
    return this.http.post<Order>(
      `${this.apiUrl}/create`,
      orderRequest,
      { headers: this.getHeaders() }
    ).pipe(catchError(this.handleError));
  }

  // Xử lý lỗi
  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(() => new Error(error.message || 'Something went wrong'));
  }
}
