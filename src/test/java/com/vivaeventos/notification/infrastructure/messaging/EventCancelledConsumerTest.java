package com.vivaeventos.notification.infrastructure.messaging;

import com.vivaeventos.notification.application.usecase.SendCancellationNotificationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventCancelledConsumerTest {

    @Mock
    private SendCancellationNotificationUseCase sendNotificationUseCase;

    @InjectMocks
    private EventCancelledConsumer consumer;

    private EventCancelledMessage message;

    @BeforeEach
    void setUp() {
        message = new EventCancelledMessage(
                UUID.randomUUID(),
                "Evento Test",
                "organizador@test.com",
                "Cancelación por prueba",
                Instant.now()
        );
    }

    @Test
    void shouldProcessMessageSuccessfully() {
        consumer.consumeEventCancelled(message);
        verify(sendNotificationUseCase, times(1)).execute(message);
    }

    @Test
    void shouldHandleUseCaseException() {
        doThrow(new RuntimeException("Error procesando notificaciones"))
                .when(sendNotificationUseCase).execute(any(EventCancelledMessage.class));

        consumer.consumeEventCancelled(message);
        verify(sendNotificationUseCase, times(1)).execute(message);
    }

    @Test
    void shouldHandleNullMessage() {

        try {
            consumer.consumeEventCancelled(null);
        } catch (NullPointerException e) {
        }
        verify(sendNotificationUseCase, never()).execute(any());
    }
}