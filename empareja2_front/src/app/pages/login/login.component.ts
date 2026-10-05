import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = ''; password = '';
  error = signal('');
  constructor(private router: Router) {}

  entrar() {
    if (!this.email.includes('@') || !this.password) {
      this.error.set('Escribe tu correo y tu contraseña para entrar.');
      return;
    }
    this.router.navigate(['/universidades']); // Sin conexión aún: luego irá POST /api/auth/login
  }
}
