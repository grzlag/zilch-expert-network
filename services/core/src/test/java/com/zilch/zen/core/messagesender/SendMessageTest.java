package com.zilch.zen.core.messagesender;


import com.zilch.zen.core.users.entity.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import testutils.IntegrationBaseTest;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
public class SendMessageTest extends IntegrationBaseTest {

    private final BlockingQueue<ConsumerRecord<String, String>> consumerRecords = new LinkedBlockingQueue<>();;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    // key -> property name
    // value -> topic name
    public static Map<String, String> topicsToOverride;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        for (var entry : topicsToOverride.entrySet()) {
            registry.add(entry.getKey(), entry::getValue);
        }
    }

    @BeforeAll
    static void setUp() {
        topicsToOverride = Map.of(
                "spring.kafka.consumers.payments.topic-name",
                UUID.randomUUID().toString(),
                "spring.kafka.consumers.users.topic-name",
                UUID.randomUUID().toString(),
                "spring.kafka.producers.payments-send-message.topic-name",
                "payments-topic-test"
        );
    }

    @Test
    @Transactional// Ensures an active Hibernate session, for lazy loading and fetching purchases list
    public void shouldSendMessage() {
        // given
        String userId = UUID.randomUUID().toString();
        createUser(userId);

        String propertyForTopicName = "spring.kafka.consumers.payments.topic-name";
        String paymentId = UUID.randomUUID().toString();
        String purchaseId = UUID.randomUUID().toString();
        String message = """
                {
                  "id": "%s",
                  "userId": "%s",
                  "purchaseId": "%s",
                  "purchaseName": "Headphones",
                  "amount": 250.75,
                  "paymentMethod": "FOUR_WEEKS",
                  "payment": "TWO"
                }""".formatted(paymentId, userId, purchaseId);

        // when
        kafkaTemplate.send(topicsToOverride.get(propertyForTopicName), message);

        // then
        await()
                .atMost(20, SECONDS)
                .pollInterval(1, SECONDS)
                .until(() -> consumerRecords.size() == 1);
    }

    private void createUser(String userId) {
        // given
        String propertyForTopicName = "spring.kafka.consumers.users.topic-name";
        String message = """
                {
                    "id": "%s",
                    "username": "John Doe",
                    "email": "john@example.com"
                }
                """.formatted(userId);

        kafkaTemplate.send(topicsToOverride.get(propertyForTopicName), message);
        await()
                .atMost(10, SECONDS)
                .pollInterval(1, SECONDS)
                .until(() -> userRepository.findById(userId).isPresent());
    }

    @KafkaListener(topics = "payments-topic-test", groupId = "testGroup")
    public void listen(ConsumerRecord<String, String> record) {
        consumerRecords.add(record);
    }
}
