package kr.hhplus.be.server;

import kr.hhplus.be.server.domain.pay.usecase.PayUseCase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaProducerConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private PayUseCase payUseCase;

    private final String topic = "order-status-topic";

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String expectedMessage = "Order completed: 12345";

        Thread consumerThread = new Thread(() -> {
            KafkaConsumer<String, String> consumer = createConsumer();
            consumer.subscribe(List.of(topic));

            try {
                consumer.poll(1000).forEach(record -> {
                    if (record.value().equals(expectedMessage)) {
                        latch.countDown();
                    }
                });
            } finally {
                consumer.close();
            }
        });
        consumerThread.start();

        // 메시지 전송
        kafkaTemplate.send(topic, expectedMessage);

        boolean received = latch.await(10, TimeUnit.SECONDS);

        assertTrue(received, "Consumer가 메시지를 받지 못했습니다.");
    }

    private KafkaConsumer<String, String> createConsumer() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "order-status-group");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new KafkaConsumer<>(consumerProps);
    }
}
