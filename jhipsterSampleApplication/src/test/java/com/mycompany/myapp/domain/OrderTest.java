package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CustomerTestSamples.*;
import static com.mycompany.myapp.domain.OrderDetailTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void orderDetailTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderDetail orderDetailBack = getOrderDetailRandomSampleGenerator();

        order.addOrderDetail(orderDetailBack);
        assertThat(order.getOrderDetails()).containsOnly(orderDetailBack);
        assertThat(orderDetailBack.getOrder()).isEqualTo(order);

        order.removeOrderDetail(orderDetailBack);
        assertThat(order.getOrderDetails()).doesNotContain(orderDetailBack);
        assertThat(orderDetailBack.getOrder()).isNull();

        order.orderDetails(new HashSet<>(Set.of(orderDetailBack)));
        assertThat(order.getOrderDetails()).containsOnly(orderDetailBack);
        assertThat(orderDetailBack.getOrder()).isEqualTo(order);

        order.setOrderDetails(new HashSet<>());
        assertThat(order.getOrderDetails()).doesNotContain(orderDetailBack);
        assertThat(orderDetailBack.getOrder()).isNull();
    }

    @Test
    void customerTest() {
        Order order = getOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        order.setCustomer(customerBack);
        assertThat(order.getCustomer()).isEqualTo(customerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }
}
