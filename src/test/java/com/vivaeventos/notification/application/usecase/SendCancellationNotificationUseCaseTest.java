package com.vivaeventos.notification.application.usecase;

import com.vivaeventos.notification.domain.port.EmailSenderPort;
import com.vivaeventos.notification.infrastructure.client.OrderServiceClient;
import com.vivaeventos.notification.infrastructure.messaging.EventCancelledMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendCancellationNotificationUseCaseTest {

    @Mock
    private OrderServiceClient orderServiceClient;

    @Mock
    private EmailSenderPort emailSender;

    @InjectMocks
    private SendCancellationNotificationUseCase useCase;

    private UUID eventId;
    private EventCancelledMessage message;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        message = new EventCancelledMessage(
                eventId,
                "Concierto de Rock",
                "organizador@test.com",
                "Problemas de logística",
                Instant.now()
        );
    }

    @Test
    void shouldSendNotificationsToAllBuyers() {
        List<String> buyerEmails = List.of("comprador1@test.com", "comprador2@test.com");
        when(orderServiceClient.getBuyerEmailsByEvent(eventId)).thenReturn(buyerEmails);
        doNothing().when(emailSender).sendEmail(anyString(), anyString(), anyString());

        useCase.execute(message);

        verify(orderServiceClient, times(1)).getBuyerEmailsByEvent(eventId);
        verify(emailSender, times(2)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotSendNotificationsWhenNoBuyers() {
        when(orderServiceClient.getBuyerEmailsByEvent(eventId)).thenReturn(List.of());

        useCase.execute(message);

        verify(orderServiceClient, times(1)).getBuyerEmailsByEvent(eventId);
        verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldContinueSendingIfOneEmailFails() {
        List<String> buyerEmails = List.of("comprador1@test.com", "comprador2@test.com", "comprador3@test.com");
        when(orderServiceClient.getBuyerEmailsByEvent(eventId)).thenReturn(buyerEmails);

        doNothing().when(emailSender).sendEmail(eq("comprador1@test.com"), anyString(), anyString());
        doThrow(new RuntimeException("Error SMTP"))
                .when(emailSender)
                .sendEmail(eq("comprador2@test.com"), anyString(), anyString());
        doNothing().when(emailSender).sendEmail(eq("comprador3@test.com"), anyString(), anyString());

        useCase.execute(message);

        verify(emailSender, times(3)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldHandleWhenOrderServiceFails() {
        when(orderServiceClient.getBuyerEmailsByEvent(eventId))
                .thenThrow(new RuntimeException("Error de conexión"));


        useCase.execute(message);

        verify(orderServiceClient, times(1)).getBuyerEmailsByEvent(eventId);
        verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldHandleNullEmailList() {
        when(orderServiceClient.getBuyerEmailsByEvent(eventId)).thenReturn(null);


        useCase.execute(message);

        verify(orderServiceClient, times(1)).getBuyerEmailsByEvent(eventId);
        verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldBuildCorrectEmailBody() {
        List<String> buyerEmails = List.of("test@test.com");
        when(orderServiceClient.getBuyerEmailsByEvent(eventId)).thenReturn(buyerEmails);
        doNothing().when(emailSender).sendEmail(anyString(), anyString(), anyString());

        useCase.execute(message);

        verify(emailSender).sendEmail(
                eq("test@test.com"),
                eq(" Evento Cancelado: Concierto de Rock"),
                anyString()
        );
    }
}