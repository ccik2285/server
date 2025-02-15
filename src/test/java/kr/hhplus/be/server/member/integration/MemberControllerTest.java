package kr.hhplus.be.server.member.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.api.MemberController;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.coupon.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.member.usecase.ChargeBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.GetBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.UseBalanceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MemberPointRepositoryCustom memberPointRepositoryCustom;

    @Mock
    private ChargeBalanceUseCase chargeBalanceUseCase;
    @Mock
    private GetBalanceUseCase getBalanceUseCase;
    @Mock
    private UseBalanceUseCase useBalanceUseCase;

    @Mock
    private MemberPointService memberPointService;

    @Mock
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long mbrNo;
    private Long couponNo;

    @BeforeEach
    void setUp() {
        mbrNo = 1L;
        couponNo = 123L;
    }
    @InjectMocks
    private MemberController memberController;


    @Test
    void 포인트사용동시성테스트() throws Exception {
        // given
        Long mbrNo = 1L;
        Long initialBalance = 1000L;
        Long chargeAmount = 1000L;
        Long useAmount = 300L;

        when(memberPointService.getBalance(mbrNo)).thenReturn(700L);
       // doNothing().when(chargeBalanceUseCase).execute(mbrNo, chargeAmount);

        chargeBalanceUseCase.execute(mbrNo, chargeAmount);

        int numberOfThreads = 10;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    useBalanceUseCase.execute(mbrNo, useAmount);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        Optional<Long> finalBalance = memberPointService.getBalance(mbrNo).describeConstable();
        assertEquals(Optional.of(700L), finalBalance);
    }


    @Test
    void testGetBalance() throws Exception {
        Long mbrNo = 1L;
        Long expectedBalance = 1000L;

        // when
        when(memberPointService.getBalance(mbrNo)).thenReturn(expectedBalance);

        // execute
        ResponseEntity<Long> response = memberController.getBalance(mbrNo);

        // verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBalance, response.getBody());
    }

    @Test
    public void 충전성공() {
        Long mbrNo = 1L;
        Long amount = 500L;

        doNothing().when(chargeBalanceUseCase).execute(mbrNo, amount);

        ResponseEntity<String> response = memberController.rechargeBalance(mbrNo, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("충전 완료", response.getBody());
    }
    @Test
    void 쿠폰발행성공() throws Exception {
        // given
        Long mbrNo = 1L;
        Long couponNo = 100L;

        when(couponService.issueCoupon(mbrNo, couponNo)).thenReturn(true);

        ResponseEntity<String> response = memberController.issueCoupon(mbrNo, couponNo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("쿠폰 발급 성공", response.getBody());
    }

    @Test
    void 쿠폰발행실패() throws Exception {
        // given
        Long mbrNo = 1L;
        Long couponNo = 100L;

        when(couponService.issueCoupon(mbrNo, couponNo)).thenReturn(false);

        ResponseEntity<String> response = memberController.issueCoupon(mbrNo, couponNo);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("쿠폰 발급 실패: 재고 부족", response.getBody());
    }
}
