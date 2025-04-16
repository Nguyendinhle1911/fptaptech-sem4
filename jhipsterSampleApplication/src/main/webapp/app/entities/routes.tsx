import React from 'react';
import { Route } from 'react-router-dom'; // Đã thay đổi từ 'react-router' sang 'react-router-dom' (best practice)

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Customer from './customer';
import Product from './product';
import Order from './order';
import OrderDetail from './order-detail';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="customer/*" element={<Customer />} />
        <Route path="product/*" element={<Product />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-detail/*" element={<OrderDetail />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
