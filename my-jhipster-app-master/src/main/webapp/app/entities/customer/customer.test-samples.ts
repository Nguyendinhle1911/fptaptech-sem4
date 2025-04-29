import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  firstName: 'Lenora',
  lastName: 'Little',
  email: 'John_Ullrich@gmail.com',
};

export const sampleWithPartialData: ICustomer = {
  id: 21246,
  firstName: 'Murphy',
  lastName: 'Grimes',
  email: 'Terrence.Mills@hotmail.com',
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  firstName: 'Lyla',
  lastName: 'Langosh',
  email: 'Conor_Powlowski15@hotmail.com',
  phone: '787-506-7917 x264',
  address: 'astride outlandish',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Irma',
  lastName: 'Davis',
  email: 'Rosa71@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
