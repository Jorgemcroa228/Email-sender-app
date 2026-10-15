package com.emailsender.controller;

import com.emailsender.dto.EmailRequest;
import com.emailsender.dto.EmailResponse;
import com.emailsender.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Email API", description = "Endpoints para envío de correos electrónicos via SendGrid")
public class EmailController {

    private final EmailService emailService;

    // ============================================================
    //  GET /api/email/health — Health Check
    // ============================================================
    @GetMapping("/health")
    @Operation(
            summary = "Health Check",
            description = "Verifica que el servicio de correo esté activo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Servicio activo")
            }
    )
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Email Sender API",
                "provider", "SendGrid",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // ============================================================
    //  POST /api/email/send — Enviar correo simple / HTML
    // ============================================================
    @PostMapping("/send")
    @Operation(
            summary = "Enviar correo",
            description = "Envía un correo electrónico usando SendGrid. Soporta texto plano y HTML, con CC y BCC opcionales.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmailRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Correo simple",
                                            value = """
                                                    {
                                                      "to": ["destinatario@ejemplo.com"],
                                                      "subject": "Hola desde Spring Boot!",
                                                      "body": "Este es un correo de prueba.",
                                                      "htmlContent": false
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Correo HTML con CC",
                                            value = """
                                                    {
                                                      "to": ["destinatario@ejemplo.com"],
                                                      "cc": ["copia@ejemplo.com"],
                                                      "subject": "Correo HTML de prueba",
                                                      "body": "<h1>Hola!</h1><p>Este es un correo <strong>HTML</strong>.</p>",
                                                      "htmlContent": true
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente",
                            content = @Content(schema = @Schema(implementation = EmailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "500", description = "Error al enviar el correo")
            }
    )
    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody EmailRequest request) {
        log.info("=== Solicitud POST /api/email/send recibida ===");
        log.info("Para: {}", request.getTo());
        log.info("Asunto: {}", request.getSubject());

        EmailResponse response = emailService.sendEmail(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }
}
