package com.vivaeventos.notification.infrastructure.messaging;

import com.vivaeventos.notification.application.usecase.SendCancellationNotificationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventCancelledConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventCancelledConsumer.class);
    private final SendCancellationNotificationUseCase sendNotificationUseCase;

    public EventCancelledConsumer(SendCancellationNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EVENTO_CANCELADO)
    public void consumeEventCancelled(EventCancelledMessage message) {
        log.info(" Mensaje recibido - Evento cancelado: eventId={}, eventName={}",
                message.getEventId(), message.getEventName());

        try {
            sendNotificationUseCase.execute(message);
            log.info(" Notificaciones procesadas para evento: {}", message.getEventId());
        } catch (Exception e) {
            log.error(" Error procesando notificaciones para evento {}: {}",
                    message.getEventId(), e.getMessage(), e);
        }
    }
}