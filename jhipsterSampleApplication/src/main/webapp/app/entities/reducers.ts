import customer from 'app/entities/customer/customer.reducer';
import product from 'app/entities/product/product.reducer';
import order from 'app/entities/order/order.reducer';
import orderDetail from 'app/entities/order-detail/order-detail.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  customer,
  product,
  order,
  orderDetail,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
