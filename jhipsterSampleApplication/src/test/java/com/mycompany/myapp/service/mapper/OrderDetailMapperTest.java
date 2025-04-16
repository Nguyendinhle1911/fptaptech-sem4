package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.OrderDetailAsserts.*;
import static com.mycompany.myapp.domain.OrderDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderDetailMapperTest {

    private OrderDetailMapper orderDetailMapper;

    @BeforeEach
    void setUp() {
        orderDetailMapper = new OrderDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderDetailSample1();
        var actual = orderDetailMapper.toEntity(orderDetailMapper.toDto(expected));
        assertOrderDetailAllPropertiesEquals(expected, actual);
    }
}
