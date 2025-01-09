package kr.hhplus.be.server.domain.member.usecase;

import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChargeBalanceUseCase {

    private final MemberPointRepositoryCustom memberPointRepositoryCustom;

    public ChargeBalanceUseCase(MemberPointRepositoryCustom memberPointRepositoryCustom) {
        this.memberPointRepositoryCustom = memberPointRepositoryCustom;
    }

    @Transactional
    public void execute(Long mbrNo, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        memberPointRepositoryCustom.chargeBalance(mbrNo, amount);
    }
}
