package com.vivaeventos.notification.infrastructure.email;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Test
    void shouldSendEmailSuccessfully() throws Exception {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        EmailService service =
                new EmailService(mailSender);

        Field field =
                EmailService.class.getDeclaredField("fromEmail");

        field.setAccessible(true);
        field.set(service, "noreply@test.com");

        service.sendEmail(
                "user@test.com",
                "subject",
                "body"
        );

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldThrowExceptionWhenMailSenderFails() throws Exception {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        doThrow(new RuntimeException("error"))
                .when(mailSender)
                .send(any(org.springframework.mail.SimpleMailMessage.class));

        EmailService service =
                new EmailService(mailSender);

        Field field =
                EmailService.class.getDeclaredField("fromEmail");

        field.setAccessible(true);
        field.set(service, "noreply@test.com");

        assertThrows(
                RuntimeException.class,
                () -> service.sendEmail(
                        "user@test.com",
                        "subject",
                        "body"
                )
        );
    }
}