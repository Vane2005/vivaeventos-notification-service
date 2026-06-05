package com.vivaeventos.notification.infrastructure.messaging;

import java.util.List;

public record TicketGeneratedMessage(
        String customerEmail,
        List<String> qrCodes
) {}
