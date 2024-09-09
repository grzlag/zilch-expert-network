package com.zilch.zen.core.payments;

import com.zilch.zen.core.payments.entity.Payment;
import com.zilch.zen.core.payments.entity.PaymentRepository;
import com.zilch.zen.core.users.entity.UserRepository;
import com.zilch.zen.core.utils.WhichPayment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import testutils.IntegrationBaseTest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentCreationTest extends IntegrationBaseTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

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
                UUID.randomUUID().toString()
        );
    }

    @Test
    @Transactional// Ensures an active Hibernate session, for lazy loading and fetching purchases list
    public void shouldCreatePurchase() {
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
                .atMost(10, SECONDS)
                .pollInterval(1, SECONDS)
                .until(() -> paymentRepository.findById(paymentId).isPresent());
        Payment savedPayment = paymentRepository.findById(paymentId).orElse(null);
        assertNotNull(savedPayment);
        assertEquals(new BigDecimal("250.75"), savedPayment.getAmount());
        assertEquals(WhichPayment.TWO, savedPayment.getPayment());
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
}
