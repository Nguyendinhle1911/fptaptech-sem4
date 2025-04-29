import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 26110,
  orderDate: dayjs('2025-04-16T19:17'),
  totalAmount: 8208,
  status: 'until',
};

export const sampleWithPartialData: IOrder = {
  id: 19840,
  orderDate: dayjs('2025-04-16T18:44'),
  totalAmount: 6042,
  status: 'extract',
};

export const sampleWithFullData: IOrder = {
  id: 27813,
  orderDate: dayjs('2025-04-16T14:31'),
  totalAmount: 7492,
  status: 'correctly',
};

export const sampleWithNewData: NewOrder = {
  orderDate: dayjs('2025-04-16T12:58'),
  totalAmount: 21114,
  status: 'for',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
