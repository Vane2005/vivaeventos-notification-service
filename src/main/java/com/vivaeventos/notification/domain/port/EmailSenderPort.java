package com.vivaeventos.notification.domain.port;

public interface EmailSenderPort {
    void sendEmail(String to, String subject, String body);
}