package com.vivaeventos.notification.infrastructure.messaging;

import com.vivaeventos.notification.application.usecase.SendPaymentNotificationUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.*;

class PaymentApprovedConsumerTest {

    @Test
    void shouldInvokeUseCase() {

        SendPaymentNotificationUseCase useCase =
                mock(SendPaymentNotificationUseCase.class);

        PaymentApprovedConsumer consumer =
                new PaymentApprovedConsumer(useCase);

        PaymentApprovedMessage message =
                new PaymentApprovedMessage(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        BigDecimal.TEN,
                        "COP",
                        "REF",
                        "user@test.com",
                        Instant.now()
                );

        consumer.consume(message);

        verify(useCase).execute(message);
    }
}