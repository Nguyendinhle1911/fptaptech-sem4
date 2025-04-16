export interface ICustomer {
  id?: number;
  name?: string;
  email?: string;
  phone?: string | null;
  address?: string | null;
}

export const defaultValue: Readonly<ICustomer> = {};
