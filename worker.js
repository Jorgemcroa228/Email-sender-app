/**
 * Cloudflare Worker -> SendGrid
 *
 * Variables / secretos requeridos (NO se escriben en el código):
 *   SENDGRID_API_KEY  (secret)  -> tu API key de SendGrid
 *   FROM_EMAIL        (var)     -> remitente verificado en SendGrid
 *   AUTH_TOKEN        (secret)  -> token propio para proteger este endpoint
 *
 * Uso:
 *   POST /  con header  Authorization: Bearer <AUTH_TOKEN>
 *   Body JSON: { "to": "dest@correo.com", "subject": "Hola", "text": "Mensaje", "html": "<p>Mensaje</p>" }
 */

const SENDGRID_URL = "https://api.sendgrid.com/v3/mail/send";

const json = (data, status = 200) =>
  new Response(JSON.stringify(data), {
    status,
    headers: { "Content-Type": "application/json" },
  });

export default {
  async fetch(request, env) {
    if (request.method !== "POST") {
      return json({ error: "Usa el método POST" }, 405);
    }

    // Protección básica para que nadie más use tu cuenta de SendGrid
    const auth = request.headers.get("Authorization");
    if (!env.AUTH_TOKEN || auth !== `Bearer ${env.AUTH_TOKEN}`) {
      return json({ error: "No autorizado" }, 401);
    }

    let body;
    try {
      body = await request.json();
    } catch {
      return json({ error: "JSON inválido" }, 400);
    }

    const { to, subject, text, html } = body;
    if (!to || !subject || (!text && !html)) {
      return json({ error: "Faltan campos: to, subject y text o html" }, 400);
    }

    const content = [];
    if (text) content.push({ type: "text/plain", value: text });
    if (html) content.push({ type: "text/html", value: html });

    const payload = {
      personalizations: [{ to: [{ email: to }] }],
      from: { email: env.FROM_EMAIL },
      subject,
      content,
    };

    try {
      const res = await fetch(SENDGRID_URL, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${env.SENDGRID_API_KEY}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      // SendGrid responde 202 Accepted sin cuerpo cuando todo va bien
      if (res.status === 202) {
        return json({ ok: true, message: "Correo enviado" });
      }

      const detail = await res.text();
      return json({ ok: false, status: res.status, detail }, 502);
    } catch (err) {
      return json({ ok: false, error: err.message }, 500);
    }
  },
};
