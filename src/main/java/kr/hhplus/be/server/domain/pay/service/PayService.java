package kr.hhplus.be.server.domain.pay.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import kr.hhplus.be.server.domain.pay.dto.request.PayDetailRequest;
import kr.hhplus.be.server.domain.pay.repository.PayRepositoryCustom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayService {
    private final PayRepositoryCustom payRepositoryCustom;
    private final OrderRepositoryCustom orderRepositoryCustom;

    public PayService(PayRepositoryCustom payRepositoryCustom, OrderRepositoryCustom orderRepositoryCustom) {
        this.payRepositoryCustom = payRepositoryCustom;
        this.orderRepositoryCustom = orderRepositoryCustom;
    }

    @Transactional
    public void processPayments(Long ordDtlNo, List<PayDetailRequest> pay) {
        for (PayDetailRequest payInfo : pay) {
            payRepositoryCustom.payOrder(ordDtlNo, payInfo.getPayTypeCd(),
                    payInfo.getPayAmount(), payInfo.getPayStateCd());
        }
        boolean isUpdated = orderRepositoryCustom.updateOrderStatus(ordDtlNo, OrderStateCd.COMPLETED);

        if (!isUpdated) {
            throw new RuntimeException("주문 상태 업데이트에 실패했습니다.");
        }
    }
}
