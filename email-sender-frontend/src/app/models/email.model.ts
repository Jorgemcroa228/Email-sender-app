export interface EmailRequest {
  to: string[];
  cc?: string[];
  bcc?: string[];
  subject: string;
  body: string;
  htmlContent: boolean;
}

export interface EmailResponse {
  success: boolean;
  message: string;
  statusCode: number;
  timestamp: string;
}
