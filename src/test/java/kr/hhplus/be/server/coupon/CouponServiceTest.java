package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.CouponStateCd;
import kr.hhplus.be.server.common.CouponTypeCd;
import kr.hhplus.be.server.domain.coupon.models.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import kr.hhplus.be.server.domain.member.repository.MemberCouponRepositoryCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberCouponRepositoryCustom memberCouponRepositoryCustom;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private IssueCouponUseCase issueCouponUseCase;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    public void 쿠폰정상발급테스트() {
        // Given
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

        when(couponRepository.findById(couponNo)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .mbrNo(mbrNo)
                .couponNo(couponNo)
                .couponTypeCd(coupon.getCouponTypeCd())
                .couponStateCd(CouponStateCd.AVAILABLE)
                .build();

        when(memberCouponRepositoryCustom.save(any(MemberCoupon.class))).thenReturn(memberCoupon);
        when(zSetOperations.add(anyString(), anyString(), anyDouble())).thenReturn(true);

        // When
        issueCouponUseCase.execute(mbrNo, couponNo);

        // Then
        assertEquals(9, coupon.getStockQuantity());
        verify(couponRepository, times(1)).save(coupon);
        verify(memberCouponRepositoryCustom, times(1)).save(any(MemberCoupon.class));
    }

    @Test
    public void 쿠폰발급선착순_동시성테스트() throws InterruptedException {
        // Given
        Long couponNo = 1L;
        Coupon coupon = Coupon.builder()
                .coupon_no(couponNo)
                .couponTypeCd(CouponTypeCd.PERCENT_COUPON)
                .percentDiscount(10L)
                .immediateDiscount(0L)
                .totQuantity(100)
                .stockQuantity(1)
                .build();

        when(couponRepository.findById(couponNo)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);
        when(zSetOperations.add(anyString(), anyString(), anyDouble())).thenReturn(true);
        when(redisTemplate.opsForZSet().range("coupon:1", 0, -1)).thenReturn(Set.of("1", "2"));

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        Runnable task = () -> {
            try {
                issueCouponUseCase.execute(1L, couponNo);
            } finally {
                latch.countDown();
            }
        };

        // When
        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);

        latch.await();

        Set<String> members = redisTemplate.opsForZSet().range("coupon:1", 0, -1);
        System.out.println("Members in ZSet: " + members);

        // Then
        assertEquals(0, coupon.getStockQuantity()); // 2개 소진 후 0이 되어야 함
        verify(couponRepository, times(1)).save(coupon);
        verify(memberCouponRepositoryCustom, times(1)).save(any(MemberCoupon.class));

        executorService.shutdown();
    }

}
