package com.vivaeventos.notification.application.usecase;

import com.vivaeventos.notification.domain.port.EmailSenderPort;
import com.vivaeventos.notification.infrastructure.messaging.PaymentApprovedMessage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SendPaymentNotificationUseCaseTest {

    @Test
    void shouldSendPaymentConfirmationEmail() {

        EmailSenderPort emailSender = mock(EmailSenderPort.class);

        SendPaymentNotificationUseCase useCase =
                new SendPaymentNotificationUseCase(emailSender);

        PaymentApprovedMessage message =
                new PaymentApprovedMessage(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        BigDecimal.valueOf(100),
                        "COP",
                        "REF123",
                        "test@test.com",
                        Instant.now()
                );

        useCase.execute(message);

        verify(emailSender).sendEmail(
                eq("test@test.com"),
                contains("Compra confirmada"),
                contains(message.orderId().toString())
        );
    }
}