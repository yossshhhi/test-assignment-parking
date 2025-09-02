package com.example.parking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Сервис управления парковкой", version = "1.0"))
@SpringBootApplication
public class ParkingApplication {

    /**
     * Метод - точка входа.
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
    }
}
