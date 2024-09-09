package testutils;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static testutils.containers.KafkaTestContainer.setupKafkaContainer;
import static testutils.containers.PostgresTestContainer.setupPostgresContainer;

// Singleton containers approach to define a containers that are only started once
// https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/

// If you are using colima then yoy may need to:
//    sudo ln -sf $HOME/.colima/default/docker.sock /var/run/docker.sock
//    colima stop
//    colima start --network-address
//
// https://golang.testcontainers.org/system_requirements/using_colima/

public class IntegrationBaseTest {

    private static final PostgreSQLContainer<?> postgresSQLContainer;
    private static final KafkaContainer kafkaContainer;

    static {
        postgresSQLContainer = setupPostgresContainer();
        kafkaContainer = setupKafkaContainer();
    }

}
