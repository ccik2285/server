package kr.hhplus.be.server.domain.order.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepositoryCustom orderRepositoryCustom;

    public OrderService(OrderRepositoryCustom orderRepositoryCustom) {
        this.orderRepositoryCustom = orderRepositoryCustom;
    }

    @Transactional
    public Long createOrder(Long mbrNo, List<OrderDetailRequest> orderDetails) {
        Long ordNo = orderRepositoryCustom.createOrder(mbrNo, LocalDateTime.now());

        for (OrderDetailRequest detail : orderDetails) {
            boolean success = orderRepositoryCustom.decreaseStock(detail.getGoodsNo(), detail.getOrderQuantity());
            if (!success) {
                throw new IllegalStateException("재고 부족: goods_no = " + detail.getGoodsNo());
            }
            orderRepositoryCustom.createOrderDetail(ordNo, detail);
        }
        return ordNo;
    }
}
