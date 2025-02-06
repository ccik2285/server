package kr.hhplus.be.server.domain.coupon.usecase;

import kr.hhplus.be.server.domain.coupon.models.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import kr.hhplus.be.server.domain.member.repository.MemberCouponRepositoryCustom;
import kr.hhplus.be.server.common.CouponStateCd;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class IssueCouponUseCase {

    private final CouponRepository couponRepository;
    private final MemberCouponRepositoryCustom memberCouponRepositoryCustom;
    private final StringRedisTemplate redisTemplate;

    private static final String COUPON_KEY_PREFIX = "coupon:";

    public IssueCouponUseCase(CouponRepository couponRepository, MemberCouponRepositoryCustom memberCouponRepositoryCustom, StringRedisTemplate redisTemplate) {
        this.couponRepository = couponRepository;
        this.memberCouponRepositoryCustom = memberCouponRepositoryCustom;
        this.redisTemplate = redisTemplate;
    }

    @Async
    @Transactional
    public boolean execute(Long mbrNo, Long couponNo) {
        String redisKey = COUPON_KEY_PREFIX + couponNo;
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        String uniqueScore = UUID.randomUUID().toString();
        Long rank = zSetOperations.add(redisKey, mbrNo.toString(), uniqueScore.hashCode()) ?
                zSetOperations.rank(redisKey, mbrNo.toString()) : -9999;

        if (rank == null || rank >= getCouponLimit(couponNo)) {
            return false;
        }

        Optional<Coupon> optionalCoupon = couponRepository.findById(couponNo);
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

    private long getCouponLimit(Long couponNo) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponNo);
        return optionalCoupon.map(Coupon::getTotQuantity).orElse(0L);
    }
}