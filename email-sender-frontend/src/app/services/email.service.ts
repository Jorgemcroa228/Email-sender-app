import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmailRequest, EmailResponse } from '../models/email.model';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private readonly apiUrl = 'http://localhost:8080/api/email';

  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {}

  /**
   * Envía un correo electrónico via la API de Spring Boot
   */
  sendEmail(request: EmailRequest): Observable<EmailResponse> {
    return this.http.post<EmailResponse>(
      `${this.apiUrl}/send`,
      request,
      { headers: this.headers }
    );
  }

  /**
   * Verifica que el backend esté activo
   */
  healthCheck(): Observable<any> {
    return this.http.get(`${this.apiUrl}/health`);
  }
}
