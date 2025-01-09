package kr.hhplus.be.server.domain.coupon.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.CouponStateCd;
import kr.hhplus.be.server.domain.coupon.models.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import kr.hhplus.be.server.domain.member.repository.MemberCouponRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@Slf4j
public class CouponService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponRepository couponRepository;
    private final MemberCouponRepositoryCustom memberCouponRepositoryCustom;

    public CouponService(CouponRepository couponRepository, MemberCouponRepositoryCustom memberCouponRepositoryCustom) {
        this.couponRepository = couponRepository;
        this.memberCouponRepositoryCustom = memberCouponRepositoryCustom;
    }


    @Transactional
    public boolean issueCoupon(Long mbrNo, Long couponNo) {
        Optional<Coupon> optionalCoupon = couponRepository.findByIdForUpdate(couponNo);

        if (optionalCoupon.isEmpty()) {
            return false;
        }

        Coupon coupon = optionalCoupon.get();
        logger.info("hello : " + coupon.getStockQuantity());
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
