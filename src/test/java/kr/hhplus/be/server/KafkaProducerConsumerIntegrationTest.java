package kr.hhplus.be.server;

import kr.hhplus.be.server.common.KafkaConsumer;
import kr.hhplus.be.server.domain.pay.usecase.PayUseCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
@EnableKafka
public class KafkaProducerConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private PayUseCase payUseCase;

    @Autowired
    private KafkaConsumer kafkaConsumer;


    private final String topic = "order-status-topic";
    private CountDownLatch latch = new CountDownLatch(1);
    private String receivedMessage;

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        String expectedMessage = "Order completed: 12345";

        kafkaTemplate.send(topic, expectedMessage);
    }

}
