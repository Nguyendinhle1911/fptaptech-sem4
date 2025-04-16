package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.OrderDetail;
import com.mycompany.myapp.repository.OrderDetailRepository;
import com.mycompany.myapp.service.OrderDetailService;
import com.mycompany.myapp.service.dto.OrderDetailDTO;
import com.mycompany.myapp.service.mapper.OrderDetailMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.OrderDetail}.
 */
@Service
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    private final OrderDetailRepository orderDetailRepository;

    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderDetailMapper orderDetailMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailMapper = orderDetailMapper;
    }

    @Override
    public OrderDetailDTO save(OrderDetailDTO orderDetailDTO) {
        LOG.debug("Request to save OrderDetail : {}", orderDetailDTO);
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        orderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public OrderDetailDTO update(OrderDetailDTO orderDetailDTO) {
        LOG.debug("Request to update OrderDetail : {}", orderDetailDTO);
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        orderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public Optional<OrderDetailDTO> partialUpdate(OrderDetailDTO orderDetailDTO) {
        LOG.debug("Request to partially update OrderDetail : {}", orderDetailDTO);

        return orderDetailRepository
            .findById(orderDetailDTO.getId())
            .map(existingOrderDetail -> {
                orderDetailMapper.partialUpdate(existingOrderDetail, orderDetailDTO);

                return existingOrderDetail;
            })
            .map(orderDetailRepository::save)
            .map(orderDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> findAll() {
        LOG.debug("Request to get all OrderDetails");
        return orderDetailRepository.findAll().stream().map(orderDetailMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDetailDTO> findOne(Long id) {
        LOG.debug("Request to get OrderDetail : {}", id);
        return orderDetailRepository.findById(id).map(orderDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderDetail : {}", id);
        orderDetailRepository.deleteById(id);
    }
}
