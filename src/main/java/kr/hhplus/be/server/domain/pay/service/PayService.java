package kr.hhplus.be.server.domain.pay.service;

import kr.hhplus.be.server.domain.pay.usecase.PayUseCase;
import kr.hhplus.be.server.domain.pay.dto.request.PayDetailRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayService {

    private final PayUseCase payUseCase;

    public PayService(PayUseCase payUseCase) {
        this.payUseCase = payUseCase;
    }

    public void processPayments(Long mbrNo, Long ordDtlNo, List<PayDetailRequest> pay) {
        payUseCase.execute(mbrNo, ordDtlNo, pay);
    }
}
