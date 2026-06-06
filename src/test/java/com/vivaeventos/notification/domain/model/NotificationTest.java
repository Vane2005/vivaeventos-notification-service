package com.vivaeventos.notification.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void shouldCreateNotificationAndAccessFields() {

        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Instant now = Instant.now();

        Notification notification = new Notification(
                id,
                eventId,
                "Concierto",
                "user@test.com",
                "Subject",
                "Body",
                Notification.NotificationStatus.PENDING,
                null,
                null,
                now
        );

        assertEquals(id, notification.id());
        assertEquals(eventId, notification.eventId());
        assertEquals("Concierto", notification.eventName());
        assertEquals("user@test.com", notification.recipientEmail());
        assertEquals("Subject", notification.subject());
        assertEquals("Body", notification.body());
        assertEquals(Notification.NotificationStatus.PENDING, notification.status());
        assertNull(notification.failureReason());
        assertNull(notification.sentAt());
        assertEquals(now, notification.createdAt());
    }

    @Test
    void shouldChangeStatusToSent() {

        Instant created = Instant.now();
        Instant sent = Instant.now();

        Notification notification = new Notification(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Evento",
                "user@test.com",
                "Subject",
                "Body",
                Notification.NotificationStatus.PENDING,
                null,
                null,
                created
        );

        Notification updated =
                notification.withStatus(
                        Notification.NotificationStatus.SENT,
                        sent
                );

        assertEquals(Notification.NotificationStatus.SENT, updated.status());
        assertEquals(sent, updated.sentAt());
    }

    @Test
    void shouldMarkNotificationAsFailed() {

        Notification notification = new Notification(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Evento",
                "user@test.com",
                "Subject",
                "Body",
                Notification.NotificationStatus.PENDING,
                null,
                null,
                Instant.now()
        );

        Notification failed =
                notification.withFailure(
                        "SMTP Error",
                        Instant.now()
                );

        assertEquals(
                Notification.NotificationStatus.FAILED,
                failed.status()
        );

        assertEquals(
                "SMTP Error",
                failed.failureReason()
        );

        assertNull(failed.sentAt());
    }
}