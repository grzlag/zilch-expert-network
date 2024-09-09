package com.zilch.zen.core.users;

import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.users.entity.UserRepository;
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

import java.util.Map;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class UserCreationTest extends IntegrationBaseTest {

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
                "spring.kafka.consumers.users.topic-name",
                UUID.randomUUID().toString()
        );
    }

    @Test
    @Transactional// Ensures an active Hibernate session, for lazy loading and fetching purchases list
    void shouldCreateNewUser() {
        // given
        String propertyForTopicName = "spring.kafka.consumers.users.topic-name";
        String userId = UUID.randomUUID().toString();
        String message = """
                {
                    "id": "%s",
                    "username": "John Doe",
                    "email": "john@example.com"
                }
                """.formatted(userId);

        // when
        kafkaTemplate.send(topicsToOverride.get(propertyForTopicName), message);

        // then
        await()
                .atMost(10, SECONDS)
                .pollInterval(1, SECONDS)
                .until(() -> userRepository.findById(userId).isPresent());
        User savedUser = userRepository.findById(userId).orElse(null);
        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertTrue(savedUser.getPurchases().isEmpty());
    }
}