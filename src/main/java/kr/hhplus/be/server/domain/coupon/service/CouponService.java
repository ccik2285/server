package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.usecase.IssueCouponUseCase;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final IssueCouponUseCase issueCouponUseCase;

    public CouponService(IssueCouponUseCase issueCouponUseCase) {
        this.issueCouponUseCase = issueCouponUseCase;
    }

    public boolean issueCoupon(Long mbrNo, Long couponNo) {
        return issueCouponUseCase.execute(mbrNo, couponNo);
    }
}
