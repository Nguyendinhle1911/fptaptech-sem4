package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderDetail;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.OrderDetailDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderDetail} and its DTO {@link OrderDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderDetailMapper extends EntityMapper<OrderDetailDTO, OrderDetail> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderDetailDTO toDto(OrderDetail s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
