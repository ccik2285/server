package kr.hhplus.be.server.domain.coupon.usecase;

import kr.hhplus.be.server.domain.coupon.models.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import kr.hhplus.be.server.domain.member.repository.MemberCouponRepositoryCustom;
import kr.hhplus.be.server.common.CouponStateCd;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IssueCouponUseCase {

    private final CouponRepository couponRepository;
    private final MemberCouponRepositoryCustom memberCouponRepositoryCustom;

    public IssueCouponUseCase(CouponRepository couponRepository, MemberCouponRepositoryCustom memberCouponRepositoryCustom) {
        this.couponRepository = couponRepository;
        this.memberCouponRepositoryCustom = memberCouponRepositoryCustom;
    }

    @Transactional
    public boolean execute(Long mbrNo, Long couponNo) {
        Optional<Coupon> optionalCoupon = couponRepository.findByIdForUpdate(couponNo);

        if (optionalCoupon.isEmpty()) {
            return false;
        }

        Coupon coupon = optionalCoupon.get();
        if (coupon.getStockQuantity() <= 0) {
            return false;
        }

        coupon.decreaseStock();
        couponRepository.save(coupon);

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
