package kr.hhplus.be.server.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@EnableKafka
public class KafkaConsumer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @KafkaListener(topics = "order-status-topic", groupId = "order-status-group")
    public void consumeOrderStatus(String message) {
        String result = "Order completed: " + message;
        kafkaTemplate.send("order-status-topic", result);
    }
}
