package com.vivaeventos.notification.application.usecase;

import com.vivaeventos.notification.domain.port.EmailSenderPort;
import com.vivaeventos.notification.infrastructure.messaging.PaymentApprovedMessage;
import org.springframework.stereotype.Service;

@Service
public class SendPaymentNotificationUseCase {

    private final EmailSenderPort emailSender;

    public SendPaymentNotificationUseCase(
            EmailSenderPort emailSender) {
        this.emailSender = emailSender;
    }

    public void execute(PaymentApprovedMessage message) {

        String subject = "Compra confirmada - VivaEventos";

        String body = """
                Hola,

                Tu compra fue confirmada exitosamente.

                Orden: %s

                Valor pagado: %s %s

                Gracias por usar VivaEventos.
                """
                .formatted(
                        message.orderId(),
                        message.amount(),
                        message.currency()
                );

        emailSender.sendEmail(
                message.customerEmail(),
                subject,
                body
        );
    }
}