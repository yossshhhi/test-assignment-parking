package com.example.parking;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static PostgreSQLContainer postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.4"))
                .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"))
                .withDatabaseName("parking-tests-db")
                .withUsername("sa")
                .withPassword("sa");
        setMaxConnections(postgreSQLContainer);
        postgreSQLContainer.start();
    }

    private static void setMaxConnections(PostgreSQLContainer postgreSQLContainer2) {
        postgreSQLContainer2.setCommand("postgres", "-c", "max_connections=500");
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}
