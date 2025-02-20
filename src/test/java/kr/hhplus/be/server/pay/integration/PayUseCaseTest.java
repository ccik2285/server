package kr.hhplus.be.server.pay.integration;

import kr.hhplus.be.server.domain.pay.dto.request.PayDetailRequest;
import kr.hhplus.be.server.domain.pay.repository.PayRepositoryCustom;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.pay.usecase.PayUseCase;
import kr.hhplus.be.server.common.KafkaConsumer;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@EmbeddedKafka(partitions = 1, topics = {"order-status-topic"})
class PayUseCaseTest {

    @Spy
    @InjectMocks
    private PayUseCase payUseCase;

    @Mock
    private PayRepositoryCustom payRepositoryCustom;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

    @Mock
    private MemberPointService memberPointService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Mock
    private KafkaConsumer kafkaConsumer;

    // Mockito 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
    }

    @Test
    void 결제성공_외부플랫폼_통신테스트() throws InterruptedException {
        // given
        Long mbrNo = 1L;
        Long ordDtlNo = 100L;
        PayDetailRequest payDetail1 = new PayDetailRequest(PayTypeCd.POINT, 1000L);
        PayDetailRequest payDetail2 = new PayDetailRequest(PayTypeCd.POINT, 2000L);

        List<PayDetailRequest> payDetails = Arrays.asList(payDetail1, payDetail2);

        // when
        when(payRepositoryCustom.payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(1000L), eq(PayStateCd.PAYED))).thenReturn(true);
        when(payRepositoryCustom.payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(2000L), eq(PayStateCd.PAYED))).thenReturn(true);
        when(orderRepositoryCustom.updateOrderStatus(eq(ordDtlNo), eq(OrderStateCd.COMPLETED))).thenReturn(true);

        payUseCase.execute(mbrNo, ordDtlNo, payDetails);

        // Kafka로 메시지가 전송되었는지 확인
        verify(kafkaTemplate, times(1)).send(eq("order-status-topic"), anyString());

        // DB 업데이트와 관련된 호출 확인
        verify(payRepositoryCustom, times(1)).payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(1000L), eq(PayStateCd.PAYED));
        verify(payRepositoryCustom, times(1)).payOrder(eq(ordDtlNo), eq(PayTypeCd.POINT), eq(2000L), eq(PayStateCd.PAYED));
        verify(memberPointService, times(1)).useBalance(eq(mbrNo), eq(3000L));
        verify(orderRepositoryCustom, times(1)).updateOrderStatus(eq(ordDtlNo), eq(OrderStateCd.COMPLETED));

    }
}
