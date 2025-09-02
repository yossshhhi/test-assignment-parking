# Parking Service (Liquibase)

## Запуск
1. `docker compose up -d db`
2. `./mvnw spring-boot:run` (или `mvn spring-boot:run`)
3. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Конфигурация
- PostgreSQL: `spring.datasource.*`
- Liquibase: `spring.liquibase.change-log=classpath:db/migration/liquibase-changeLog.xml`
- `parking.capacity` — вместимость парковки.

## Версии API
- **v1**: `occupied/free + averageStaySeconds`
- **v2**: `entries/exits + averageStaySeconds`
