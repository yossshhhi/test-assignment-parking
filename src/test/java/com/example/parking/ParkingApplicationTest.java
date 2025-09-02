package com.example.parking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
@Testcontainers(disabledWithoutDocker = true)
public class ParkingApplicationTest {

	@Test
	public void contextLoads() {
	}
}
