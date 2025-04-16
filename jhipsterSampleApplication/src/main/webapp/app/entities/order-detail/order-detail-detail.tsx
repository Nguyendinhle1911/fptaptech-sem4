import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-detail.reducer';

export const OrderDetailDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderDetailEntity = useAppSelector(state => state.orderDetail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.detail.title">OrderDetail</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderDetailEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderDetailEntity.quantity}</dd>
          <dt>
            <span id="unitPrice">
              <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.unitPrice">Unit Price</Translate>
            </span>
          </dt>
          <dd>{orderDetailEntity.unitPrice}</dd>
          <dt>
            <span id="subTotal">
              <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.subTotal">Sub Total</Translate>
            </span>
          </dt>
          <dd>{orderDetailEntity.subTotal}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.product">Product</Translate>
          </dt>
          <dd>{orderDetailEntity.product ? orderDetailEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.order">Order</Translate>
          </dt>
          <dd>{orderDetailEntity.order ? orderDetailEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-detail" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-detail/${orderDetailEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetailDetail;
