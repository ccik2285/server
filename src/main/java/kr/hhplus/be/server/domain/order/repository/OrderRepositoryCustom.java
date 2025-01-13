package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import java.time.LocalDateTime;

public interface OrderRepositoryCustom {

    Long createOrder(Long mbrNo, LocalDateTime createdAt);
    Long createOrderDetail(Long ordNo, OrderDetailRequest orderData);
    boolean updateOrderStatus(Long ordNo, OrderStateCd orderStateCd);
    boolean decreaseStock(Long goodsNo, Long quantity);
}
