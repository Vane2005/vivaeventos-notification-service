package com.vivaeventos.notification.application.usecase;

import com.vivaeventos.notification.domain.port.EmailSenderPort;
import com.vivaeventos.notification.infrastructure.client.OrderServiceClient;
import com.vivaeventos.notification.infrastructure.messaging.EventCancelledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SendCancellationNotificationUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendCancellationNotificationUseCase.class);
    private final OrderServiceClient orderServiceClient;
    private final EmailSenderPort emailSender;

    public SendCancellationNotificationUseCase(OrderServiceClient orderServiceClient,
                                               EmailSenderPort emailSender) {
        this.orderServiceClient = orderServiceClient;
        this.emailSender = emailSender;
    }

    public void execute(EventCancelledMessage message) {
        log.info("Procesando notificaciones para evento cancelado: {}", message.getEventName());

        List<String> buyerEmails;
        try {
            buyerEmails = orderServiceClient.getBuyerEmailsByEvent(message.getEventId());
            // Manejar null
            if (buyerEmails == null) {
                buyerEmails = Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Error obteniendo emails de compradores para evento {}: {}",
                    message.getEventId(), e.getMessage());
            return;
        }

        if (buyerEmails.isEmpty()) {
            log.info("No hay compradores registrados para el evento: {}", message.getEventId());
            return;
        }

        log.info("Se encontraron {} compradores para notificar", buyerEmails.size());

        String subject = buildSubject(message);
        String body = buildBody(message);

        int successCount = 0;
        int failureCount = 0;

        for (String email : buyerEmails) {
            try {
                emailSender.sendEmail(email, subject, body);
                successCount++;
                log.info(" Notificación enviada a: {}", email);
            } catch (Exception e) {
                failureCount++;
                log.error(" Error enviando notificación a {}: {}", email, e.getMessage());
            }
        }

        log.info("Resumen - Exitosas: {}, Fallidas: {}", successCount, failureCount);
    }

    private String buildSubject(EventCancelledMessage message) {
        return String.format(" Evento Cancelado: %s", message.getEventName());
    }

    private String buildBody(EventCancelledMessage message) {
        return String.format("""
                ========================================
                          VIVAEVENTOS
                ========================================
                
                Estimado cliente,
                
                Lamentamos informarle que el evento:
                
                 "%s"
                
                ha sido CANCELADO.
                
                ┌─────────────────────────────────────┐
                │ Detalles de la cancelación:         │
                ├─────────────────────────────────────┤
                │ Motivo: %s                          │
                │ Fecha: %s                           │
                │ Cancelado por: %s                   │
                └─────────────────────────────────────┘
                
                 El reembolso será procesado automáticamente
                   en los próximos 5-7 días hábiles.
                
                 Si tiene preguntas, contacte a:
                   soporte@vivaeventos.com
                
                ──────────────────────────────────────
                © 2024 VivaEventos - Tu plataforma de eventos
                ========================================
                """,
                message.getEventName(),
                message.getReason(),
                message.getCancelledAt(),
                message.getCancelledBy()
        );
    }
}