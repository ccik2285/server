package kr.hhplus.be.server.domain.member.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import org.springframework.stereotype.Service;

@Service
public class MemberPointService {
    private final MemberPointRepositoryCustom memberPointRepositoryCustom;

    public MemberPointService(MemberPointRepositoryCustom memberPointRepositoryCustom) {
        this.memberPointRepositoryCustom = memberPointRepositoryCustom;
    }

    public Long getBalance(Long mbrNo) {
        return memberPointRepositoryCustom.getBalance(mbrNo);
    }

    @Transactional
    public void chargeBalance(Long mbrNo, Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        memberPointRepositoryCustom.chargeBalance(mbrNo, amount);
    }

    @Transactional
    public void useBalance(Long mbrNo, Long amount){
        Long currentBalance = getBalance(mbrNo);

        if(amount > currentBalance){
            throw new IllegalArgumentException("사용할 수 있는 포인트가 부족합니다.");
        }
        memberPointRepositoryCustom.useBalance(mbrNo, amount);
    }
}
