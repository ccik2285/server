package kr.hhplus.be.server.domain.member.usecase;

import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import org.springframework.stereotype.Service;

@Service
public class GetBalanceUseCase {

    private final MemberPointRepositoryCustom memberPointRepositoryCustom;

    public GetBalanceUseCase(MemberPointRepositoryCustom memberPointRepositoryCustom) {
        this.memberPointRepositoryCustom = memberPointRepositoryCustom;
    }

    public Long execute(Long mbrNo) {
        return memberPointRepositoryCustom.getBalance(mbrNo);
    }
}
