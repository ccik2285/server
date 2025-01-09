package kr.hhplus.be.server.domain.coupon.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.CouponStateCd;
import kr.hhplus.be.server.domain.coupon.models.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepositoryCustom;
import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import kr.hhplus.be.server.domain.member.repository.MemberCouponRepositoryCustom;
import java.util.Optional;

public class CouponService {

    private final CouponRepositoryCustom couponRepositoryCustom;
    private final MemberCouponRepositoryCustom memberCouponRepositoryCustom;

    public CouponService(CouponRepositoryCustom couponRepositoryCustom, MemberCouponRepositoryCustom memberCouponRepositoryCustom) {
        this.couponRepositoryCustom = couponRepositoryCustom;
        this.memberCouponRepositoryCustom = memberCouponRepositoryCustom;
    }

    @Transactional
    public boolean issueCoupon(Long mbrNo, Long couponNo) {
        Optional<Coupon> optionalCoupon = couponRepositoryCustom.findByIdForUpdate(couponNo);

        if (optionalCoupon.isEmpty()) {
            return false;
        }

        Coupon coupon = optionalCoupon.get();
        if (coupon.getStockQuantity() <= 0) {
            return false;
        }

        coupon.decreaseStock();
        couponRepositoryCustom.save(coupon);
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .mbrNo(mbrNo)
                .couponNo(couponNo)
                .couponTypeCd(coupon.getCouponTypeCd())
                .couponStateCd(CouponStateCd.AVAILABLE)
                .build();

        memberCouponRepositoryCustom.save(memberCoupon);
        return true;
    }
}
