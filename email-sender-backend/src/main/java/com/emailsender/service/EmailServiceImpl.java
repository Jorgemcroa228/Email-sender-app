package com.emailsender.service;

import com.emailsender.dto.EmailRequest;
import com.emailsender.dto.EmailResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    @Override
    public EmailResponse sendEmail(EmailRequest request) {
        log.info("Enviando correo a: {}", request.getTo());

        try {
            // --- Remitente ---
            Email from = new Email(fromEmail, fromName);

            // --- Tipo de contenido ---
            String contentType = request.isHtmlContent() ? "text/html" : "text/plain";
            Content content = new Content(contentType, request.getBody());

            // --- Construir Mail ---
            Mail mail = new Mail();
            mail.setFrom(from);
            mail.setSubject(request.getSubject());
            mail.addContent(content);

            // --- Personalización (destinatarios) ---
            Personalization personalization = new Personalization();

            // TO
            List<String> toList = request.getTo();
            for (String toAddress : toList) {
                personalization.addTo(new Email(toAddress));
            }

            // CC (opcional)
            if (request.getCc() != null && !request.getCc().isEmpty()) {
                for (String ccAddress : request.getCc()) {
                    personalization.addCc(new Email(ccAddress));
                }
            }

            // BCC (opcional)
            if (request.getBcc() != null && !request.getBcc().isEmpty()) {
                for (String bccAddress : request.getBcc()) {
                    personalization.addBcc(new Email(bccAddress));
                }
            }

            mail.addPersonalization(personalization);

            // --- Enviar con SendGrid ---
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request sgRequest = new Request();
            sgRequest.setMethod(Method.POST);
            sgRequest.setEndpoint("mail/send");
            sgRequest.setBody(mail.build());

            Response response = sg.api(sgRequest);

            log.info("SendGrid response status: {}", response.getStatusCode());
            log.debug("SendGrid response body: {}", response.getBody());

            // SendGrid devuelve 202 Accepted cuando el correo fue aceptado
            if (response.getStatusCode() == 202) {
                log.info("Correo enviado exitosamente a: {}", request.getTo());
                return EmailResponse.ok("Correo enviado exitosamente a " + String.join(", ", request.getTo()));
            } else {
                log.error("Error de SendGrid. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
                return EmailResponse.error(
                        "Error al enviar el correo. Status SendGrid: " + response.getStatusCode() + ". Detalle: " + response.getBody(),
                        response.getStatusCode()
                );
            }

        } catch (IOException e) {
            log.error("IOException al enviar correo: {}", e.getMessage(), e);
            return EmailResponse.error("Error de conexión con SendGrid: " + e.getMessage(), 500);
        } catch (Exception e) {
            log.error("Error inesperado al enviar correo: {}", e.getMessage(), e);
            return EmailResponse.error("Error inesperado: " + e.getMessage(), 500);
        }
    }
}
