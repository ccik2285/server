package kr.hhplus.be.server.domain.pay.usecase;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import kr.hhplus.be.server.domain.pay.dto.request.PayDetailRequest;
import kr.hhplus.be.server.domain.pay.repository.PayRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PayUseCase {

    private final PayRepositoryCustom payRepositoryCustom;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final MemberPointService memberPointService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String MOCK_API_URL = "https://mockapi.com/payments";

    public PayUseCase(PayRepositoryCustom payRepositoryCustom, OrderRepositoryCustom orderRepositoryCustom,
                      MemberPointService memberPointService, KafkaTemplate<String, String> kafkaTemplate) {
        this.payRepositoryCustom = payRepositoryCustom;
        this.orderRepositoryCustom = orderRepositoryCustom;
        this.memberPointService = memberPointService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void execute(Long mbrNo, Long ordDtlNo, List<PayDetailRequest> pay) {
        Long totalAmt = 0L;
        for (PayDetailRequest payInfo : pay) {
            payRepositoryCustom.payOrder(ordDtlNo, payInfo.getPayTypeCd(), payInfo.getPayAmount(), PayStateCd.PAYED);
            totalAmt += payInfo.getPayAmount();
        }

        memberPointService.useBalance(mbrNo, totalAmt);

        boolean isUpdated = orderRepositoryCustom.updateOrderStatus(ordDtlNo, OrderStateCd.COMPLETED);
        if (!isUpdated) {
            throw new RuntimeException("주문 상태 업데이트에 실패했습니다.");
        }

        sendOrderToKafka(ordDtlNo);
    }

    private void sendOrderToKafka(Long ordDtlNo) {
        String message = "Order completed: " + ordDtlNo;
        kafkaTemplate.send("order-status-topic", message);
        log.info("Kafka로 주문 상태 전송 완료: {}", message);
    }
}
