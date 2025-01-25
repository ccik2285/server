package kr.hhplus.be.server.domain.order.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import kr.hhplus.be.server.domain.order.usecase.OrderUseCase;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    private final OrderUseCase orderUseCase;

    public OrderService(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    public Long createOrder(Long mbrNo, List<OrderDetailRequest> orderDetails) {
        return orderUseCase.createOrder(mbrNo, orderDetails);
    }
}
