package kr.hhplus.be.server.domain.member.usecase;

import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UseBalanceUseCase {

    private final MemberPointRepositoryCustom memberPointRepositoryCustom;
    private final GetBalanceUseCase getBalanceUseCase;

    public UseBalanceUseCase(MemberPointRepositoryCustom memberPointRepositoryCustom, GetBalanceUseCase getBalanceUseCase) {
        this.memberPointRepositoryCustom = memberPointRepositoryCustom;
        this.getBalanceUseCase = getBalanceUseCase;
    }

    @Transactional
    public void execute(Long mbrNo, Long amount) {
        Long currentBalance = getBalanceUseCase.execute(mbrNo);

        if (amount > currentBalance) {
            throw new IllegalArgumentException("사용할 수 있는 포인트가 부족합니다.");
        }

        memberPointRepositoryCustom.useBalance(mbrNo, amount);
    }
}
