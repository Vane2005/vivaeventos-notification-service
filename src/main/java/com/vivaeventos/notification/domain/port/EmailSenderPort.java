package com.vivaeventos.notification.domain.port;

import java.util.List;

public interface EmailSenderPort {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithQrAttachments(String to, List<String> qrCodes);
}