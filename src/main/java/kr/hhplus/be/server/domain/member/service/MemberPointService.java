package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.usecase.GetBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.ChargeBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.UseBalanceUseCase;
import org.springframework.stereotype.Service;

@Service
public class MemberPointService {

    private final GetBalanceUseCase getBalanceUseCase;
    private final ChargeBalanceUseCase chargeBalanceUseCase;
    private final UseBalanceUseCase useBalanceUseCase;

    public MemberPointService(GetBalanceUseCase getBalanceUseCase, ChargeBalanceUseCase chargeBalanceUseCase, UseBalanceUseCase useBalanceUseCase) {
        this.getBalanceUseCase = getBalanceUseCase;
        this.chargeBalanceUseCase = chargeBalanceUseCase;
        this.useBalanceUseCase = useBalanceUseCase;
    }

    public Long getBalance(Long mbrNo) {
        return getBalanceUseCase.execute(mbrNo);
    }

    public void chargeBalance(Long mbrNo, Long amount) {
        chargeBalanceUseCase.execute(mbrNo, amount);
    }

    public void useBalance(Long mbrNo, Long amount) {
        useBalanceUseCase.execute(mbrNo, amount);
    }
}
