# Turnos Backend (Spring Boot + MySQL + WebSocket)

**Generado:** 2025-08-18T17:26:19.367761Z

## Requisitos
- Java 17
- Maven 3.9+
- Docker (opcional, recomendado)

## 1) Levantar MySQL con Docker
```bash
docker compose up -d
```

Credenciales por defecto (ver `docker-compose.yml`):
- DB: `turnos`
- user: `root`
- pass: `root_password`

## 2) Configurar aplicación
`src/main/resources/application.yml` ya viene listo para usar variables de entorno:
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=turnos
DB_USER=turnos
DB_PASS=secret
```

## 3) Ejecutar
```bash
mvn spring-boot:run
```

## 4) Probar en el navegador
- Tablero (TV): http://localhost:8080/
- Swagger: http://localhost:8080/swagger-ui.html

## 5) Flujo de prueba (sin Angular)
1. **Mock RENIEC**  
   `POST /api/reniec/lookup/12345678`
2. **Crear ticket**  
   `POST /api/tickets` (Body: JSON con los campos del `PersonDTO`)
3. **Llamar siguiente**  
   `GET /api/tickets/next?module=1`
4. **Ver tablero** (se actualiza en tiempo real por WebSocket)

## Estructura
- `person/` entidad y repo de personas (DNI).
- `ticket/` entidad, repo y servicio de tickets.
- `api/` controladores REST + DTOs.
- `config/` WebSocket y CORS.

## Notas
- El consumo RENIEC es **mock** para la demo. Implementa un `ReniecClient` real con `WebClient` y credenciales si vas a producción.
- Si usas Angular, apúntalo a `http://localhost:8080` y habilita CORS (ya está).
