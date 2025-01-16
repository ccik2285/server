package kr.hhplus.be.server.domain.order.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderUseCase {
    private final OrderRepositoryCustom orderRepositoryCustom;

    public OrderUseCase(OrderRepositoryCustom orderRepositoryCustom) {
        this.orderRepositoryCustom = orderRepositoryCustom;
    }

    @Transactional
    public Long createOrder(Long mbrNo, List<OrderDetailRequest> orderDetails) {
        orderRepositoryCustom.createOrder(mbrNo);
        Long ordNo = orderRepositoryCustom.getOrderNo(mbrNo);
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
