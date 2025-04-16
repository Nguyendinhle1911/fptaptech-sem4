import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from './user.service'; // Tạo service mới
import { trigger, style, animate, transition } from '@angular/animations';

interface User {
  id: number;
  username: string;
  role: string;
}

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
  standalone: true,
  imports: [RouterLink, CommonModule],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(10px)' }),
        animate('300ms ease-in', style({ opacity: 1, transform: 'translateY(0)' })),
      ]),
    ]),
  ],
})
export class UserComponent implements OnInit {
  users: User[] = [];
  error: string = '';

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    const token = localStorage.getItem('token');
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to load users. Ensure you have ADMIN privileges.';
        console.error('Error details:', err);
      }
    });
  }
}
