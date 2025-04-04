import { Component, OnInit } from '@angular/core';
import { Order } from './order.service';
import { OrderService } from './order.service';
import { CurrencyPipe, DatePipe, NgForOf, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Thêm FormsModule để dùng ngModel

@Component({
  selector: 'app-order',
  imports: [
    DatePipe,
    CurrencyPipe,
    RouterLink,
    NgIf,
    NgForOf,
    FormsModule // Thêm vào imports để hỗ trợ ngModel
  ],
  templateUrl: './order.component.html',
  standalone: true,
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit {
  orders: Order[] = []; // Mảng đơn hàng gốc
  filteredOrders: Order[] = []; // Mảng đơn hàng đã lọc
  errorMessage: string = '';
  searchTerm: string = ''; // Từ khóa tìm kiếm
  loading: boolean = true; // Biến để kiểm soát trạng thái loading

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true; // Bật trạng thái loading
    this.orderService.getAllOrders().subscribe({
      next: (orders) => {
        this.orders = orders;
        this.orders.sort((a, b) => {
          return new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime();
        });
        this.filteredOrders = [...this.orders]; // Khởi tạo danh sách đã lọc
        this.loading = false; // Tắt trạng thái loading
      },
      error: (error) => {
        this.errorMessage = 'Failed to load orders: ' + error.message;
        this.loading = false; // Tắt trạng thái loading khi có lỗi
      }
    });
  }

  filterOrders(): void {
    const term = this.searchTerm.toLowerCase().trim();
    if (!term) {
      // Nếu không có từ khóa, hiển thị toàn bộ danh sách đã sắp xếp
      this.filteredOrders = [...this.orders];
    } else {
      this.filteredOrders = this.orders.filter(order => {
        // Kiểm tra các trường cơ bản
        const matchesBasicFields =
          (order.id?.toString().includes(term) || '') ||
          (order.userId?.toString().includes(term) || '') ||
          order.totalPrice.toString().includes(term) ||
          order.paymentMethod.toString().includes(term) ||
          new Date(order.orderDate).toLocaleDateString().toLowerCase().includes(term) ||
          order.status.toLowerCase().includes(term);

        // Kiểm tra productId trong mảng products
        const matchesProducts = order.products.some(product =>
          product.productId.toString().includes(term)
        );

        // Trả về true nếu khớp với bất kỳ trường nào
        return matchesBasicFields || matchesProducts;
      });
    }
  }

  updateOrderStatus(orderId: number | undefined, newStatus: string): void {
    if (orderId === undefined) {
      console.error('Order ID is undefined, cannot update status.');
      return;
    }

    this.orderService.updateOrderStatusForAdmin(orderId, newStatus).subscribe({
      next: () => {
        alert(`Order #${orderId} marked as ${newStatus}`);
        this.loadOrders(); // Reload danh sách orders sau khi cập nhật
      },
      error: (error) => {
        this.errorMessage = `Failed to update order: ${error.message}`;
      }
    });
  }

  protected readonly confirm = confirm;
}
