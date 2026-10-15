import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { EmailService } from '../../services/email.service';
import { EmailRequest, EmailResponse } from '../../models/email.model';

@Component({
  selector: 'app-email-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './email-form.component.html',
  styleUrls: ['./email-form.component.css']
})
export class EmailFormComponent implements OnInit {

  // Formulario
  emailRequest: EmailRequest = {
    to: [],
    cc: [],
    bcc: [],
    subject: '',
    body: '',
    htmlContent: false
  };

  // Inputs de texto para manejar listas como strings separados por coma
  toInput: string = '';
  ccInput: string = '';
  bccInput: string = '';

  // Estado
  isLoading: boolean = false;
  response: EmailResponse | null = null;
  errorMessage: string = '';
  backendStatus: 'checking' | 'online' | 'offline' = 'checking';

  constructor(private emailService: EmailService) {}

  ngOnInit(): void {
    this.checkBackendHealth();
  }

  checkBackendHealth(): void {
    this.backendStatus = 'checking';
    this.emailService.healthCheck().subscribe({
      next: () => { this.backendStatus = 'online'; },
      error: () => { this.backendStatus = 'offline'; }
    });
  }

  onSubmit(form: NgForm): void {
    if (form.invalid) return;

    // Parsear las listas de emails desde los inputs de texto
    this.emailRequest.to = this.parseEmails(this.toInput);
    this.emailRequest.cc = this.parseEmails(this.ccInput);
    this.emailRequest.bcc = this.parseEmails(this.bccInput);

    if (this.emailRequest.to.length === 0) {
      this.errorMessage = 'Debes ingresar al menos un destinatario.';
      return;
    }

    this.isLoading = true;
    this.response = null;
    this.errorMessage = '';

    this.emailService.sendEmail(this.emailRequest).subscribe({
      next: (res) => {
        this.response = res;
        this.isLoading = false;
        if (res.success) {
          this.resetForm(form);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Error al conectar con el servidor. ¿Está el backend corriendo?';
        this.response = null;
      }
    });
  }

  private parseEmails(input: string): string[] {
    if (!input || !input.trim()) return [];
    return input.split(',')
      .map(e => e.trim())
      .filter(e => e.length > 0);
  }

  private resetForm(form: NgForm): void {
    this.toInput = '';
    this.ccInput = '';
    this.bccInput = '';
    this.emailRequest = {
      to: [],
      cc: [],
      bcc: [],
      subject: '',
      body: '',
      htmlContent: false
    };
    form.resetForm();
  }

  dismissAlert(): void {
    this.response = null;
    this.errorMessage = '';
  }
}
