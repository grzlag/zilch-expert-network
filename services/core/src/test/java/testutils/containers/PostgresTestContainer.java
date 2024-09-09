package testutils.containers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class PostgresTestContainer {

    public static PostgreSQLContainer<?> setupPostgresContainer() {
        PostgreSQLContainer<?> postgresContainer = createPostgresContainer();
        postgresContainer.start();
        waitForPostgresToStart(postgresContainer);
        setupEnvProperties(postgresContainer);
        return postgresContainer;
    }

    private static PostgreSQLContainer<?> createPostgresContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("zen-core");
    }

    private static void waitForPostgresToStart(PostgreSQLContainer<?> postgresContainer) {
        postgresContainer
                .waitingFor(Wait.forListeningPort())
                .waitingFor(Wait.forHealthcheck());
    }

    private static void setupEnvProperties(PostgreSQLContainer<?> postgresContainer) {
        System.setProperty("DB_URL", postgresContainer.getJdbcUrl());
        System.setProperty("DB_USER", postgresContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgresContainer.getPassword());
    }
}
