import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { DashboardComponent } from './page/dashboard/dashboard.component';
import { AuthGuard } from './auth.guard'; // Import AuthGuard
import { UserComponent } from './page/user/user.component';
import { ProductComponent } from './page/product/product.component';
import {UpdateProductComponent} from './page/product/update/update.component';
import {AddProductComponent} from './page/product/add/add.component';
import {OrderComponent} from './page/order/order.component';
import {OrderCreateComponent} from './page/order/order-create/order-create.component';
import {PaymentComponent} from './page/payment/payment.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent, canActivate: [AuthGuard] }, // Bảo vệ route Dashboard
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'users', component: UserComponent, canActivate: [AuthGuard]},

  { path: 'products', component: ProductComponent, canActivate: [AuthGuard]},
  { path: 'add-product', component: AddProductComponent },
  { path: 'update-product/:id', component: UpdateProductComponent },

  { path: 'orders', component: OrderComponent },
  {
    path: 'order-create',
    component: OrderCreateComponent
  },
  {
    path: 'payment/:orderId',
    component: PaymentComponent
  },

  { path: '**', redirectTo: '' }
];
