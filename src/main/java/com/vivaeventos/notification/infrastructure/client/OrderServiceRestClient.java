package com.vivaeventos.notification.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.UUID;

@Component
public class OrderServiceRestClient implements OrderServiceClient {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceRestClient.class);
    private final RestClient restClient;

    public OrderServiceRestClient(@Value("${services.order-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public List<String> getBuyerEmailsByEvent(UUID eventId) {
        try {
            var response = restClient.get()
                    .uri("/api/v1/orders/by-event/{eventId}/emails", eventId)
                    .retrieve()
                    .body(EmailsResponse.class);

            return response != null ? response.emails() : List.of();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("No se encontraron órdenes para el evento: {}", eventId);
                return List.of();
            }
            log.error("Error consultando emails para evento {}: {}", eventId, ex.getMessage());
            return List.of();
        } catch (Exception e) {
            log.error("Error inesperado consultando emails: {}", e.getMessage());
            return List.of();
        }
    }

    private record EmailsResponse(List<String> emails) {}
}