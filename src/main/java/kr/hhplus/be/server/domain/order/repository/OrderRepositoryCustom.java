package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;

public interface OrderRepositoryCustom {

    Long createOrder(Long mbrNo);
    void createOrderDetail(Long ordNo, OrderDetailRequest orderData);
    boolean updateOrderStatus(Long ordNo, OrderStateCd orderStateCd);
    boolean decreaseStock(Long goodsNo, Long quantity);
}
