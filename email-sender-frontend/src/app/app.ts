import { Component } from '@angular/core';
import { EmailFormComponent } from './components/email-form/email-form.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [EmailFormComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  title = 'Email Sender App';
}
