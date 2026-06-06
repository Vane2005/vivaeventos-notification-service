package com.vivaeventos.notification.infrastructure.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationControllerTest {

    @Test
    void shouldReturnExpectedMessage() {

        NotificationController controller =
                new NotificationController();

        String response = controller.test("hola");

        assertEquals(
                "Notification service is working: hola",
                response
        );
    }
}