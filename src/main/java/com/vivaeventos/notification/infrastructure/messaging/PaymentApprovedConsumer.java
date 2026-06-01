package com.vivaeventos.notification.infrastructure.messaging;

import com.vivaeventos.notification.application.usecase.SendPaymentNotificationUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedConsumer {

    private final SendPaymentNotificationUseCase useCase;

    public PaymentApprovedConsumer(
            SendPaymentNotificationUseCase useCase
    ) {
        this.useCase = useCase;
    }

    @RabbitListener(
            queues = RabbitMQConfig.QUEUE_PAGO_APROBADO
    )
    public void consume(
            PaymentApprovedMessage message
    ) {
        System.out.println("=== MENSAJE RECIBIDO ===");
        System.out.println(message);

        useCase.execute(message);
    }
}