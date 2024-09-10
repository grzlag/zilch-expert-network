import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class E2ETest {

    private static KafkaProducer<String, String> producer;
    private static KafkaConsumer<String, String> consumer;

    private static final String PRODUCER_USER_TOPIC = "users";
    private static final String PRODUCER_PAYMENTS_TOPIC = "payments";
    private static final String CONSUMER_TOPIC = "e2e-tests";
    private static final String KAFKA_SERVER = "localhost:9092";

    @BeforeAll
    static void setUp() {
        // Producer configuration
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Initialize Kafka Producer
        producer = new KafkaProducer<>(producerProps);

        // Consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Initialize Kafka Consumer
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(CONSUMER_TOPIC));
    }

    @AfterAll
    static void tearDown() {
        if (producer != null) {
            producer.close();
        }
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    public void testKafkaMessageSendAndReceive() throws InterruptedException {
        // Send a message to Kafka to create user
        String userId = UUID.randomUUID().toString();
        String messageValue = """
                    {
                        "id": "%s",
                        "username": "John Doe",
                        "email": "john@test.com"
                    }
                """.formatted(userId);
        producer.send(new ProducerRecord<>(PRODUCER_USER_TOPIC, userId, messageValue));
        producer.flush();
        Thread.sleep(5000);

        // Send a message to Kafka to create payment
        String paymentId = UUID.randomUUID().toString();
        String purchaseId = UUID.randomUUID().toString();
        messageValue = """
            {
                 "id": "%s",
                  "userId": "%s",
                  "purchaseId": "%s",
                  "purchaseName": "Headphones",
                  "amount": 250.75,
                  "paymentMethod": "FOUR_WEEKS",
                  "payment": "TWO"
            }
            """.formatted(paymentId, userId, purchaseId);
        producer.send(new ProducerRecord<>(PRODUCER_PAYMENTS_TOPIC, messageValue));
        producer.flush();
        Thread.sleep(5000);

        // Consume the message from Kafka to check if email was meant to be triggerred
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(15));
        ConsumerRecord<String, String> record = records.iterator().next();

        // Assert the message key and value
        assertTrue(record.value().contains("Congratulations! You have successfully pay for Headphones"));
    }
}
