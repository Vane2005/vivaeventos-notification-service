package com.vivaeventos.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Notification(
        UUID id,
        UUID eventId,
        String eventName,
        String recipientEmail,
        String subject,
        String body,
        NotificationStatus status,
        String failureReason,
        Instant sentAt,
        Instant createdAt
) {
    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED
    }

    public Notification withStatus(NotificationStatus newStatus, Instant now) {
        return new Notification(
                id, eventId, eventName, recipientEmail, subject, body,
                newStatus, failureReason,
                newStatus == NotificationStatus.SENT ? now : sentAt,
                createdAt
        );
    }

    public Notification withFailure(String reason, Instant now) {
        return new Notification(
                id, eventId, eventName, recipientEmail, subject, body,
                NotificationStatus.FAILED, reason, null, createdAt
        );
    }
}