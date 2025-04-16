export interface IProduct {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  stockQuantity?: number;
}

export const defaultValue: Readonly<IProduct> = {};
