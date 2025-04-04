import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true,
  imports: [FormsModule, RouterLink, CommonModule]
})
export class RegisterComponent {
  username: string = '';
  password: string = '';
  error: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit() {
    this.error = ''; // Reset error message
    this.authService.register(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        // Xử lý lỗi cụ thể từ backend
        this.error = err.error?.message || 'Registration failed. Email might be taken.';
      }
    });
  }
}
