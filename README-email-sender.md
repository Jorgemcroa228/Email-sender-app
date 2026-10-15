# 📧 Email Sender App

Aplicación full-stack para envío de correos electrónicos con **Spring Boot** (backend) y **Angular** (frontend), usando **SendGrid** como proveedor.

---

## 🏗️ Estructura del Proyecto

```
C:\Users\jorge\
├── email-sender-backend/    ← API REST Spring Boot
└── email-sender-frontend/   ← UI Angular
```

---

## ⚙️ Configuración Inicial (IMPORTANTE)

### 1. Obtener API Key de SendGrid

1. Crea una cuenta en [sendgrid.com](https://sendgrid.com) (gratis)
2. Ve a **Settings → API Keys → Create API Key**
3. Elige **Full Access** y copia la API Key generada
4. Verifica tu correo remitente en **Settings → Sender Authentication**

### 2. Configurar el Backend

Edita el archivo:
```
email-sender-backend/src/main/resources/application.properties
```

Reemplaza:
```properties
sendgrid.api.key=SG.TU_API_KEY_AQUI
sendgrid.from.email=tu-correo@dominio.com
sendgrid.from.name=Email Sender App
```

---

## 🚀 Cómo Ejecutar

### Backend (Spring Boot)

```bash
cd email-sender-backend
mvn spring-boot:run
```

El servidor arranca en: **http://localhost:8080**

📖 Swagger UI: **http://localhost:8080/swagger-ui.html**

### Frontend (Angular)

```bash
cd email-sender-frontend
npm install
ng serve
```

La app estará disponible en: **http://localhost:4200**

---

## 🧪 Probar en Postman

1. Abre Postman
2. Haz clic en **Import**
3. Arrastra el archivo: `email-sender-backend/Email-Sender-API.postman_collection.json`
4. ¡Listo! Tendrás todos los endpoints disponibles

### Endpoints disponibles

| Método | URL | Descripción |
|--------|-----|-------------|
| `GET`  | `http://localhost:8080/api/email/health` | Health check |
| `POST` | `http://localhost:8080/api/email/send` | Enviar correo |

### Ejemplo de Request (POST /api/email/send)

```json
{
  "to": ["destinatario@ejemplo.com"],
  "cc": ["opcional@ejemplo.com"],
  "bcc": ["oculto@ejemplo.com"],
  "subject": "Hola desde Spring Boot!",
  "body": "Este es un correo de prueba.",
  "htmlContent": false
}
```

### Campos del Request

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `to` | `string[]` | ✅ Sí | Lista de destinatarios |
| `cc` | `string[]` | ❌ No | Copia (CC) |
| `bcc` | `string[]` | ❌ No | Copia oculta (BCC) |
| `subject` | `string` | ✅ Sí | Asunto del correo |
| `body` | `string` | ✅ Sí | Cuerpo del correo |
| `htmlContent` | `boolean` | ❌ No | `true` = HTML, `false` = texto plano |

### Ejemplo de Response Exitosa

```json
{
  "success": true,
  "message": "Correo enviado exitosamente a destinatario@ejemplo.com",
  "statusCode": 200,
  "timestamp": "2026-10-14T19:00:00"
}
```

---

## 🛠️ Tecnologías

### Backend
- Java 17
- Spring Boot 3.2.5
- SendGrid Java SDK 4.10.2
- Lombok
- SpringDoc OpenAPI (Swagger)

### Frontend
- Angular 17+
- TypeScript
- CSS (Glassmorphism + Gradients)

---

## 📝 Notas

- SendGrid permite enviar **100 correos/día gratis** con el plan gratuito
- El correo remitente debe estar **verificado** en SendGrid
- Los correos enviados pueden llegar a **spam** si no tienes dominio verificado
