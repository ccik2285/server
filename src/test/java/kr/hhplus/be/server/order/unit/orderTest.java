package kr.hhplus.be.server.order.unit;


import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import kr.hhplus.be.server.domain.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class orderTest {

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;
    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문생성성공테스트() {

        Long mbrNo = 1L;
        OrderDetailRequest orderDetail = new OrderDetailRequest();
        orderDetail.setGoodsNo(1L);
        orderDetail.setOrderQuantity(2L);
        orderDetail.setPriceAmt(1000L);
        Long orderNo = 123L;


        when(orderRepositoryCustom.createOrder(anyLong(), any(LocalDateTime.class))).thenReturn(orderNo);
        when(orderRepositoryCustom.decreaseStock(anyLong(), anyLong())).thenReturn(true);
        when(orderRepositoryCustom.createOrderDetail(anyLong(), any(OrderDetailRequest.class))).thenReturn(1L);


        Long result = orderService.createOrder(mbrNo, Arrays.asList(orderDetail));

        assertNotNull(result);
        assertEquals(orderNo, result);

        // verify
        verify(orderRepositoryCustom, times(1)).createOrder(anyLong(), any(LocalDateTime.class));
        verify(orderRepositoryCustom, times(1)).decreaseStock(anyLong(), anyLong());
        verify(orderRepositoryCustom, times(1)).createOrderDetail(eq(orderNo), eq(orderDetail));
    }

    @Test
    void 재고부족으로인한주문생성실패() {
        // given
        Long mbrNo = 1L;
        OrderDetailRequest orderDetail = new OrderDetailRequest();
        orderDetail.setGoodsNo(1L);
        orderDetail.setOrderQuantity(2L);
        orderDetail.setPriceAmt(1000L);

        // Mock 메서드
        when(orderRepositoryCustom.createOrder(anyLong(), any(LocalDateTime.class))).thenReturn(123L);
        when(orderRepositoryCustom.decreaseStock(anyLong(), anyLong())).thenReturn(false);

        // when & then
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            orderService.createOrder(mbrNo, Arrays.asList(orderDetail));
        });
        assertEquals("재고 부족: goods_no = 1", thrown.getMessage());
    }
}
