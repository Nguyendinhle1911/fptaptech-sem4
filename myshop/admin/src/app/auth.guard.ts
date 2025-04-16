import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {
    const token = localStorage.getItem('token');
    if (token) {
      return true; // Cho phép truy cập nếu có token
    } else {
      this.router.navigate(['/login']); // Chuyển hướng về login nếu không có token
      return false;
    }
  }
}
