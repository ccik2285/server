package kr.hhplus.be.server.order.integration;

import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import kr.hhplus.be.server.domain.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Sql(statements = "INSERT INTO goods (goods_no, goods_state_cd, goods_nm, stock_quantity, sale_price) VALUES (1, 'ON_SALE', 'Test Goods', 30, 1000)")
public class OrderTest {

    @Autowired
    private OrderService orderService;

    private Long goodsNo = 1L;

    @Test
    public void 주문서생성_동시성테스트() throws InterruptedException {
        List<OrderDetailRequest> orderDetails = Arrays.asList(
                new OrderDetailRequest(goodsNo, 1L, 1000L),
                new OrderDetailRequest(goodsNo, 1L, 1000L),
                new OrderDetailRequest(goodsNo, 3L, 1000L),
                new OrderDetailRequest(goodsNo, 4L, 1000L),
                new OrderDetailRequest(goodsNo, 1L, 1000L)
        );

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        AtomicInteger threadIndex = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    Long orderNo = orderService.createOrder(1L, orderDetails);
                    int index = threadIndex.getAndIncrement();

                    System.out.println("Thread " + index + " - orderNo: " + orderNo);

                    assertThat(orderNo).isEqualTo(index + 1);

                } catch (IllegalStateException e) {
                    assertThat(e.getMessage()).isEqualTo("재고 부족: goods_no = " + goodsNo);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
    }
}