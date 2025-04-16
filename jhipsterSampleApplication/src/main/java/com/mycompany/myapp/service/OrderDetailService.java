package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.OrderDetailDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.OrderDetail}.
 */
public interface OrderDetailService {
    /**
     * Save a orderDetail.
     *
     * @param orderDetailDTO the entity to save.
     * @return the persisted entity.
     */
    OrderDetailDTO save(OrderDetailDTO orderDetailDTO);

    /**
     * Updates a orderDetail.
     *
     * @param orderDetailDTO the entity to update.
     * @return the persisted entity.
     */
    OrderDetailDTO update(OrderDetailDTO orderDetailDTO);

    /**
     * Partially updates a orderDetail.
     *
     * @param orderDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderDetailDTO> partialUpdate(OrderDetailDTO orderDetailDTO);

    /**
     * Get all the orderDetails.
     *
     * @return the list of entities.
     */
    List<OrderDetailDTO> findAll();

    /**
     * Get the "id" orderDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderDetailDTO> findOne(Long id);

    /**
     * Delete the "id" orderDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
