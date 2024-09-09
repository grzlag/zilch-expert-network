package testutils.containers;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainer {

    public static KafkaContainer setupKafkaContainer() {
        KafkaContainer kafkaContainer = createKafkaContainer();
        kafkaContainer.start();
        waitForKafkaContainerToStart(kafkaContainer);
        setupEnvProperties(kafkaContainer);
        return kafkaContainer;
    }

    private static KafkaContainer createKafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));
    }

    private static void waitForKafkaContainerToStart(KafkaContainer kafkaContainer) {
        kafkaContainer
                .waitingFor(Wait.forListeningPort())
                .waitingFor(Wait.forHealthcheck());
    }

    private static void setupEnvProperties(KafkaContainer kafkaContainer) {
        System.setProperty("KAFKA_URL", kafkaContainer.getBootstrapServers());
    }
}
