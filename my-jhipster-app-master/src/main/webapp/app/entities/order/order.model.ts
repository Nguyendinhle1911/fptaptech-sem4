import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IOrder {
  id: number;
  orderDate?: dayjs.Dayjs | null;
  totalAmount?: number | null;
  status?: string | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
