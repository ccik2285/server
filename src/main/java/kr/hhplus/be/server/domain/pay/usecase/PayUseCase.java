package kr.hhplus.be.server.domain.pay.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import kr.hhplus.be.server.domain.pay.dto.request.PayDetailRequest;
import kr.hhplus.be.server.domain.pay.repository.PayRepositoryCustom;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayUseCase {

    private final PayRepositoryCustom payRepositoryCustom;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final MemberPointService memberPointService;

    public PayUseCase(PayRepositoryCustom payRepositoryCustom, OrderRepositoryCustom orderRepositoryCustom, MemberPointService memberPointService) {
        this.payRepositoryCustom = payRepositoryCustom;
        this.orderRepositoryCustom = orderRepositoryCustom;
        this.memberPointService = memberPointService;
    }

    @Transactional
    public void execute(Long mbrNo, Long ordDtlNo, List<PayDetailRequest> pay) {
        Long totalAmt = 0L;
        for (PayDetailRequest payInfo : pay) {
            payRepositoryCustom.payOrder(ordDtlNo, payInfo.getPayTypeCd(), payInfo.getPayAmount(), PayStateCd.PAYED);
            totalAmt += payInfo.getPayAmount();
        }

        memberPointService.useBalance(mbrNo, totalAmt);

        boolean isUpdated = orderRepositoryCustom.updateOrderStatus(ordDtlNo, OrderStateCd.COMPLETED);

        if (!isUpdated) {
            throw new RuntimeException("주문 상태 업데이트에 실패했습니다.");
        }
    }
}
