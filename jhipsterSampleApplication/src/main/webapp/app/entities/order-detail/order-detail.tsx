import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './order-detail.reducer';

export const OrderDetail = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const orderDetailList = useAppSelector(state => state.orderDetail.entities);
  const loading = useAppSelector(state => state.orderDetail.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="order-detail-heading" data-cy="OrderDetailHeading">
        <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.home.title">Order Details</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/order-detail/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.home.createLabel">Create new Order Detail</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {orderDetailList && orderDetailList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.quantity">Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quantity')} />
                </th>
                <th className="hand" onClick={sort('unitPrice')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.unitPrice">Unit Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('unitPrice')} />
                </th>
                <th className="hand" onClick={sort('subTotal')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.subTotal">Sub Total</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subTotal')} />
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.product">Product</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.order">Order</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderDetailList.map((orderDetail, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/order-detail/${orderDetail.id}`} color="link" size="sm">
                      {orderDetail.id}
                    </Button>
                  </td>
                  <td>{orderDetail.quantity}</td>
                  <td>{orderDetail.unitPrice}</td>
                  <td>{orderDetail.subTotal}</td>
                  <td>{orderDetail.product ? <Link to={`/product/${orderDetail.product.id}`}>{orderDetail.product.id}</Link> : ''}</td>
                  <td>{orderDetail.order ? <Link to={`/order/${orderDetail.order.id}`}>{orderDetail.order.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/order-detail/${orderDetail.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/order-detail/${orderDetail.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/order-detail/${orderDetail.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="jhipsterSampleApplicationApp.orderDetail.home.notFound">No Order Details found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default OrderDetail;
