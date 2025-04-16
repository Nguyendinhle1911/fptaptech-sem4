// src/app/add-product/add-product.component.ts
import { Component } from '@angular/core';
import { ProductService } from '../product.service';
import {Router, RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import { trigger, style, animate, transition } from '@angular/animations';

@Component({
  imports: [RouterLink, CommonModule, FormsModule],
  selector: 'app-add-product',
  styleUrls: ['./add.component.scss'],
  templateUrl: './add.component.html',
  animations: [
    trigger('fadeIn', [
      transition('void => in', [
        style({opacity: 0, transform: 'translateY(20px)'}),
        animate('500ms ease-in', style({opacity: 1, transform: 'translateY(0)'})),
      ]),
    ]),
  ],
  standalone: true
})

export class AddProductComponent {
  product = {  name: '', price: 0, stock: 0, description: '' };
  submitted = false;

  constructor(private productService: ProductService, protected router: Router) { }

// src/app/add-product/add-product.component.ts
  addProduct(): void {
    this.submitted = true;
    if (!this.product.name || this.product.price < 0 || this.product.stock < 0) {
      alert('Please fill in all required fields correctly');
      return;
    }

    this.productService.addProduct(this.product).subscribe({
      next: () => {
        alert('Product added successfully');
        this.router.navigate(['/products']);
      },
      error: (err) => {
        console.error('Error adding product:', err);
        alert('Failed to add product: ' + (err.message || 'Check console for details'));
      }
    });
  }
}
