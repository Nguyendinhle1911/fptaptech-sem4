import { IProduct } from 'app/shared/model/product.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderDetail {
  id?: number;
  quantity?: number;
  unitPrice?: number;
  subTotal?: number;
  product?: IProduct | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IOrderDetail> = {};
