package com.vivaeventos.notification.infrastructure.client;

import java.util.List;
import java.util.UUID;

public interface OrderServiceClient {
    List<String> getBuyerEmailsByEvent(UUID eventId);
}