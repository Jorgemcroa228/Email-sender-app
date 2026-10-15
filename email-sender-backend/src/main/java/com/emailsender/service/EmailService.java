package com.emailsender.service;

import com.emailsender.dto.EmailRequest;
import com.emailsender.dto.EmailResponse;

public interface EmailService {

    /**
     * Envía un correo electrónico usando SendGrid.
     * @param request datos del correo a enviar
     * @return respuesta con el resultado del envío
     */
    EmailResponse sendEmail(EmailRequest request);
}
