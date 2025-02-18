package kr.hhplus.be.server.kafka_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class KafkaIntegrationTest {
    private static final String TEST_TOPIC = "test-topic";
    private static final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @KafkaListener(topics = "test-topic", groupId = "my-group")
    public void listen(String message) {
        log.info("Received message: {}", message);
        messages.add(message);
    }

    @Test
    void testKafkaMessageSendAndReceive() throws InterruptedException {
        //Producer 메시지 전송
        String testMessage = "Hello Kafka Test!";
        kafkaTemplate.send(TEST_TOPIC, testMessage);
        log.info("Sent message: {}", testMessage);

        //Consumer가 메시지 정상적으로 수신했는지 확인
        String receivedMessage = messages.poll(5, TimeUnit.SECONDS);
        assertThat(receivedMessage).isEqualTo(testMessage);
    }
}
