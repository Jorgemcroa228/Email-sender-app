package com.emailsender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EmailRequest {

    @NotEmpty(message = "La lista de destinatarios no puede estar vacía")
    private List<@Email(message = "Formato de email inválido") String> to;

    private List<@Email(message = "Formato de CC inválido") String> cc;

    private List<@Email(message = "Formato de BCC inválido") String> bcc;

    @NotBlank(message = "El asunto es obligatorio")
    private String subject;

    @NotBlank(message = "El contenido del correo es obligatorio")
    private String body;

    /**
     * Si es true, el body se interpretará como HTML.
     * Por defecto: false (texto plano)
     */
    private boolean htmlContent = false;
}
