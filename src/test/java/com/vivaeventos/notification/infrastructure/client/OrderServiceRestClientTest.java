package com.vivaeventos.notification.infrastructure.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderServiceRestClientTest {

    @Test
    void shouldCreateClient() {

        OrderServiceRestClient client =
                new OrderServiceRestClient(
                        "http://localhost:8080"
                );

        assertNotNull(client);
    }
}