import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [
    RouterLink
  ],
  standalone: true
})
export class DashboardComponent {
  username: string = '';

  constructor(private router: Router) {
    // Get token from localStorage
    const token = localStorage.getItem('token');

    if (token) {
      try {
        // Decode the token
        const decoded: any = jwtDecode(token);
        // Extract username (adjust this based on your JWT structure)
        this.username = decoded.username || decoded.sub || 'User';
      } catch (e) {
        console.error('Error decoding token:', e);
        this.username = 'User';
      }
    }
  }

  logout() {
    localStorage.removeItem('token'); // Xóa token khỏi localStorage
    this.router.navigate(['/login']); // Chuyển hướng về trang login
  }

  navigateToUsers() {
    this.router.navigate(['/users']);
  }

  navigateToProducts() {
    this.router.navigate(['/products']);
  }

  navigateToOrders() {
    this.router.navigate(['/orders']);
  }
}
