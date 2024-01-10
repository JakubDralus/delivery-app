package org.company.modules.product_order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.company.modules.order.domain.Order;
import org.company.modules.product.domain.Product;

@Entity
@Getter
@Setter
public class ProductOrder {
    @EmbeddedId
    private ProductOrderPk id;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
    private int quantity;
    private Double subtotal;//TODO: think it over if this is necessary
}