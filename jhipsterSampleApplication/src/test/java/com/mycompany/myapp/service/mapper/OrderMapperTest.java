package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.OrderAsserts.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderSample1();
        var actual = orderMapper.toEntity(orderMapper.toDto(expected));
        assertOrderAllPropertiesEquals(expected, actual);
    }
}
