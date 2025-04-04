// src/app/update-product/update-product.component.ts
import { Component, OnInit } from '@angular/core';
import { ProductService, Product } from '../product.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import { trigger, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-update-product',
  templateUrl: './update.component.html',
  imports: [
    FormsModule,
    NgIf,
    RouterLink
  ],
  styleUrls: ['./update.component.scss'],
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

export class UpdateProductComponent implements OnInit {
  product: Product = { id: 0, name: '', price: 0, stock: 0, description: '' };
  submitted = false;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    protected router: Router
  ) { }

  ngOnInit(): void {
    const productId = +this.route.snapshot.paramMap.get('id')!;
    this.productService.getProductById(productId).subscribe({
      next: (data) => {
        this.product = data;
      },
      error: (err) => {
        console.error('Error fetching product:', err);
        alert('Product not found');
        this.router.navigate(['/products']);
      }
    });
  }

  updateProduct(): void {
    this.submitted = true;
    this.productService.updateProduct(this.product.id, this.product).subscribe({
      next: () => {
        alert('Product updated successfully');
        this.router.navigate(['/products']);
      },
      error: (err) => {
        console.error('Error updating product:', err);
        alert('Failed to update product');
      }
    });
  }
}
