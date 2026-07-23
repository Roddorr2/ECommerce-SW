package com.ecommerce.app.auth.application.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCodigoVerificacion(String destinatario, String codigo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Código de verificación - Technology Fix");

            String contenidoHtml = cargarTemplate("email/codigo-verificacion.html");
            contenidoHtml = contenidoHtml.replace("{{CODIGO}}", codigo);

            helper.setText(contenidoHtml, true);
            mailSender.send(mensaje);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }

    private String cargarTemplate(String templatePath) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templatePath);
        try (var inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
