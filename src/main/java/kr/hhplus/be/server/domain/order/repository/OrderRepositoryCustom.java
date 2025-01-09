package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import java.time.LocalDateTime;

public interface OrderRepositoryCustom {

    Long createOrder(Long mbrNo, LocalDateTime createdAt);
    Long createOrderDetail(Long ordNo, OrderDetailRequest orderData);
    void updateOrder();
    boolean decreaseStock(Long goodsNo, Long quantity);
}
