package com.vivaeventos.notification.infrastructure.messaging;

import com.vivaeventos.notification.domain.port.EmailSenderPort;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketGeneratedConsumer {

    private final EmailSenderPort emailSender;

    public TicketGeneratedConsumer(EmailSenderPort emailSender) {
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "notification-service.ticket.generated")
    public void consume(TicketGeneratedMessage message) {
        emailSender.sendEmailWithQrAttachments(
                message.customerEmail(),
                message.qrCodes()
        );
    }
}
