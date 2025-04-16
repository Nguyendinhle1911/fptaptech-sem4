package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OrderDetailTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDetail.class);
        OrderDetail orderDetail1 = getOrderDetailSample1();
        OrderDetail orderDetail2 = new OrderDetail();
        assertThat(orderDetail1).isNotEqualTo(orderDetail2);

        orderDetail2.setId(orderDetail1.getId());
        assertThat(orderDetail1).isEqualTo(orderDetail2);

        orderDetail2 = getOrderDetailSample2();
        assertThat(orderDetail1).isNotEqualTo(orderDetail2);
    }

    @Test
    void productTest() {
        OrderDetail orderDetail = getOrderDetailRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderDetail.setProduct(productBack);
        assertThat(orderDetail.getProduct()).isEqualTo(productBack);

        orderDetail.product(null);
        assertThat(orderDetail.getProduct()).isNull();
    }

    @Test
    void orderTest() {
        OrderDetail orderDetail = getOrderDetailRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderDetail.setOrder(orderBack);
        assertThat(orderDetail.getOrder()).isEqualTo(orderBack);

        orderDetail.order(null);
        assertThat(orderDetail.getOrder()).isNull();
    }
}
