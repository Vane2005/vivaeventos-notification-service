package com.vivaeventos.notification.infrastructure.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @PostMapping("/test")
    public String test(@RequestBody String message) {
        return "Notification service is working: " + message;
    }
}