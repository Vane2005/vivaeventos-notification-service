package com.vivaeventos.notification.infrastructure.email;

import com.vivaeventos.notification.domain.port.EmailSenderPort;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService implements EmailSenderPort {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final QrImageService qrImageService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.qrImageService = new QrImageService();
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info(" Email enviado a: {}", to);
        } catch (Exception e) {
            log.error(" Error enviando email a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email a " + to, e);
        }
    }

    @Override
    public void sendEmailWithQrAttachments(String to, List<String> qrCodes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Tus boletas - VivaEventos");

            StringBuilder html = new StringBuilder("<h2>¡Compra confirmada!</h2>");
            html.append("<p>Adjuntamos tus boletas. Presenta el QR en la entrada del evento.</p>");
            for (int i = 0; i < qrCodes.size(); i++) {
                html.append("<p><b>Boleta ").append(i + 1).append(":</b> ").append(qrCodes.get(i)).append("</p>");
                html.append("<img src='cid:qr").append(i).append("'/><br/>");
            }
            helper.setText(html.toString(), true);

            for (int i = 0; i < qrCodes.size(); i++) {
                byte[] qrImage = qrImageService.generateQrImage(qrCodes.get(i));
                helper.addInline("qr" + i,
                        new ByteArrayResource(qrImage),
                        "image/png");
            }

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error enviando email con QRs a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email con QRs", e);
        }
    }
}