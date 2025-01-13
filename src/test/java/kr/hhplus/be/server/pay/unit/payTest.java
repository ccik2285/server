package kr.hhplus.be.server.pay.unit;

import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import kr.hhplus.be.server.domain.pay.dto.request.PayDetailRequest;
import kr.hhplus.be.server.domain.pay.repository.PayRepositoryCustom;
import kr.hhplus.be.server.domain.pay.service.PayService;
import kr.hhplus.be.server.domain.pay.usecase.PayUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class PayServiceTest {

    @InjectMocks
    private PayUseCase payUseCase;

    @Mock
    private PayRepositoryCustom payRepositoryCustom;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

    @Mock
    private MemberPointService memberPointService;

    public PayServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 결제성공_주문상태_변경성공테스트() {
        // given
        Long mbrNo = 1L;
        Long ordDtlNo = 100L;
        PayDetailRequest payDetail1 = new PayDetailRequest(PayTypeCd.POINT,1000L);
        PayDetailRequest payDetail2 = new PayDetailRequest(PayTypeCd.POINT, 2000L);

        List<PayDetailRequest> payDetails = Arrays.asList(payDetail1, payDetail2);

        when(payRepositoryCustom.payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(1000L), eq(PayStateCd.PAYED))).thenReturn(true);
        when(payRepositoryCustom.payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(2000L), eq(PayStateCd.PAYED))).thenReturn(true);
        when(orderRepositoryCustom.updateOrderStatus(eq(ordDtlNo), eq(OrderStateCd.COMPLETED))).thenReturn(true);

        payUseCase.execute(mbrNo, ordDtlNo, payDetails);

        verify(payRepositoryCustom, times(1)).payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(1000L), eq(PayStateCd.PAYED));
        verify(payRepositoryCustom, times(1)).payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(2000L), eq(PayStateCd.PAYED));
        verify(memberPointService, times(1)).useBalance(eq(mbrNo), eq(3000L));
        verify(orderRepositoryCustom, times(1)).updateOrderStatus(eq(ordDtlNo), eq(OrderStateCd.COMPLETED));
    }

    @Test
    void 결제실패_주문상태_변경실패() {
        // given
        Long mbrNo = 1L;
        Long ordDtlNo = 100L;
        PayDetailRequest payDetail1 = new PayDetailRequest(PayTypeCd.POINT,1000L);
        List<PayDetailRequest> payDetails = Arrays.asList(payDetail1);

        // when
        when(payRepositoryCustom.payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(1000L), eq(PayStateCd.PAYED))).thenReturn(true);
        when(orderRepositoryCustom.updateOrderStatus(eq(ordDtlNo), eq(OrderStateCd.COMPLETED))).thenReturn(false);


        try {
            payUseCase.execute(mbrNo, ordDtlNo, payDetails);
        } catch (RuntimeException e) {
            assertEquals("주문 상태 업데이트에 실패했습니다.", e.getMessage());
        }

        verify(payRepositoryCustom, times(1)).payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(1000L), eq(PayStateCd.PAYED));
        verify(memberPointService, times(1)).useBalance(eq(mbrNo), eq(1000L));
        verify(orderRepositoryCustom, times(1)).updateOrderStatus(eq(ordDtlNo), eq(OrderStateCd.COMPLETED));
    }
}
