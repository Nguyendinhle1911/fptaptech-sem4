package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDetailDTO.class);
        OrderDetailDTO orderDetailDTO1 = new OrderDetailDTO();
        orderDetailDTO1.setId(1L);
        OrderDetailDTO orderDetailDTO2 = new OrderDetailDTO();
        assertThat(orderDetailDTO1).isNotEqualTo(orderDetailDTO2);
        orderDetailDTO2.setId(orderDetailDTO1.getId());
        assertThat(orderDetailDTO1).isEqualTo(orderDetailDTO2);
        orderDetailDTO2.setId(2L);
        assertThat(orderDetailDTO1).isNotEqualTo(orderDetailDTO2);
        orderDetailDTO1.setId(null);
        assertThat(orderDetailDTO1).isNotEqualTo(orderDetailDTO2);
    }
}
