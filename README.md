# Notification Service - VivaEventos

Microservicio encargado de enviar notificaciones (emails) a compradores cuando un evento es cancelado.

## Responsabilidades

- Consumir mensajes `evento.cancelado` desde RabbitMQ
- Consultar a `order-service` los emails de compradores
- Enviar emails de cancelación a todos los compradores

## Flujo

1. `event-service` publica `evento.cancelado` en RabbitMQ
2. `notification-service` consume el mensaje
3. Consulta emails a `order-service`
4. Envía emails a cada comprador

## Requisitos previos

- Java 21
- Docker / Docker Compose
- RabbitMQ
- Credenciales SMTP

## Variables de entorno

| Variable | Descripción |
|----------|-------------|
| RABBITMQ_HOST | Host de RabbitMQ |
| RABBITMQ_PORT | Puerto de RabbitMQ |
| ORDER_SERVICE_URL | URL de order-service |
| EMAIL_HOST | Servidor SMTP |
| EMAIL_PORT | Puerto SMTP |
| EMAIL_USERNAME | Usuario SMTP |
| EMAIL_PASSWORD | Contraseña SMTP |

## Ejecutar localmente

```bash
# Con Docker Compose
docker-compose up

# O con Maven
./mvnw spring-boot:run