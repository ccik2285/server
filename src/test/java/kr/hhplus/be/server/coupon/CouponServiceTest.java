package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.CouponTypeCd;
import kr.hhplus.be.server.domain.coupon.models.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.member.repository.MemberCouponRepositoryCustom;
import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import kr.hhplus.be.server.common.CouponStateCd;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberCouponRepositoryCustom memberCouponRepositoryCustom;

    @InjectMocks
    private CouponService couponService;

    @Test
    public void 쿠폰발급선착순_동시성테스트() throws InterruptedException {

        Long mbrNo = 1L;
        Long couponNo = 1L;

        Coupon coupon = Coupon.builder()
                .coupon_no(couponNo)
                .couponTypeCd(CouponTypeCd.PERCENT_COUPON)
                .percentDiscount(10L)
                .immediateDiscount(0L)
                .totQuantity(100)
                .stockQuantity(1)
                .build();

        Mockito.when(couponRepository.findByIdForUpdate(couponNo)).thenReturn(Optional.of(coupon));
        Mockito.when(couponRepository.save(Mockito.any(Coupon.class))).thenReturn(coupon);

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .mbrNo(mbrNo)
                .couponNo(couponNo)
                .couponTypeCd(coupon.getCouponTypeCd())
                .couponStateCd(CouponStateCd.AVAILABLE)
                .build();

        Mockito.when(memberCouponRepositoryCustom.save(Mockito.any(MemberCoupon.class)))
                .thenReturn(memberCoupon);


        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Runnable task = () -> couponService.issueCoupon(mbrNo, couponNo);

        executorService.submit(task);
        executorService.submit(task);



        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(100);
        }

        assertEquals(0, coupon.getStockQuantity());
        Mockito.verify(couponRepository, Mockito.times(1)).save(coupon);
        Mockito.verify(memberCouponRepositoryCustom, Mockito.times(1)).save(Mockito.any(MemberCoupon.class));  // MemberCoupon이 한 번만 저장되어야 함
    }

    @Test
    public void 쿠폰정상발급테스트() {

        Long mbrNo = 1L;
        Long couponNo = 1L;

        Coupon coupon = Coupon.builder()
                .coupon_no(couponNo)
                .couponTypeCd(CouponTypeCd.PERCENT_COUPON)
                .percentDiscount(10L)
                .immediateDiscount(0L)
                .totQuantity(100)
                .stockQuantity(10)
                .build();


        Mockito.when(couponRepository.findByIdForUpdate(couponNo)).thenReturn(Optional.of(coupon));
        Mockito.when(couponRepository.save(Mockito.any(Coupon.class))).thenReturn(coupon);

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .mbrNo(mbrNo)
                .couponNo(couponNo)
                .couponTypeCd(coupon.getCouponTypeCd())
                .couponStateCd(CouponStateCd.AVAILABLE)
                .build();

        Mockito.when(memberCouponRepositoryCustom.save(Mockito.any(MemberCoupon.class)))
                .thenReturn(memberCoupon);


        boolean result = couponService.issueCoupon(mbrNo, couponNo);


        assertTrue(result);
        assertEquals(9, coupon.getStockQuantity());
        Mockito.verify(couponRepository, Mockito.times(1)).save(coupon);
        Mockito.verify(memberCouponRepositoryCustom, Mockito.times(1)).save(Mockito.any(MemberCoupon.class));  // MemberCoupon이 한 번만 저장되어야 함
    }
}