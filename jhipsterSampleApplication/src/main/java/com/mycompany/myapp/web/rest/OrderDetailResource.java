package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.OrderDetailRepository;
import com.mycompany.myapp.service.OrderDetailService;
import com.mycompany.myapp.service.dto.OrderDetailDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.OrderDetail}.
 */
@RestController
@RequestMapping("/api/order-details")
public class OrderDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDetailResource.class);

    private static final String ENTITY_NAME = "orderDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderDetailService orderDetailService;

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailResource(OrderDetailService orderDetailService, OrderDetailRepository orderDetailRepository) {
        this.orderDetailService = orderDetailService;
        this.orderDetailRepository = orderDetailRepository;
    }

    /**
     * {@code POST  /order-details} : Create a new orderDetail.
     *
     * @param orderDetailDTO the orderDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderDetailDTO, or with status {@code 400 (Bad Request)} if the orderDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderDetailDTO> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderDetail : {}", orderDetailDTO);
        if (orderDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderDetailDTO = orderDetailService.save(orderDetailDTO);
        return ResponseEntity.created(new URI("/api/order-details/" + orderDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderDetailDTO.getId().toString()))
            .body(orderDetailDTO);
    }

    /**
     * {@code PUT  /order-details/:id} : Updates an existing orderDetail.
     *
     * @param id the id of the orderDetailDTO to save.
     * @param orderDetailDTO the orderDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDetailDTO,
     * or with status {@code 400 (Bad Request)} if the orderDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> updateOrderDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderDetail : {}, {}", id, orderDetailDTO);
        if (orderDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderDetailDTO = orderDetailService.update(orderDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDetailDTO.getId().toString()))
            .body(orderDetailDTO);
    }

    /**
     * {@code PATCH  /order-details/:id} : Partial updates given fields of an existing orderDetail, field will ignore if it is null
     *
     * @param id the id of the orderDetailDTO to save.
     * @param orderDetailDTO the orderDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDetailDTO,
     * or with status {@code 400 (Bad Request)} if the orderDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderDetailDTO> partialUpdateOrderDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderDetailDTO orderDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderDetail partially : {}, {}", id, orderDetailDTO);
        if (orderDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderDetailDTO> result = orderDetailService.partialUpdate(orderDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-details} : get all the orderDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderDetails in body.
     */
    @GetMapping("")
    public List<OrderDetailDTO> getAllOrderDetails() {
        LOG.debug("REST request to get all OrderDetails");
        return orderDetailService.findAll();
    }

    /**
     * {@code GET  /order-details/:id} : get the "id" orderDetail.
     *
     * @param id the id of the orderDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderDetail : {}", id);
        Optional<OrderDetailDTO> orderDetailDTO = orderDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDetailDTO);
    }

    /**
     * {@code DELETE  /order-details/:id} : delete the "id" orderDetail.
     *
     * @param id the id of the orderDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderDetail : {}", id);
        orderDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
