import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  standalone: false,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  email = '';
  password = '';
  error = signal('');
  cargando = signal(false);

  entrar(): void {
    if (!this.email.includes('@') || !this.password) {
      this.error.set('Escribe tu correo y tu contraseña para entrar.');
      return;
    }
    this.error.set('');
    this.cargando.set(true);
    this.authService.iniciarSesion(this.email, this.password).subscribe({
      next: token => {
        this.authService.guardarToken(token);
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: () => {
        this.error.set('Correo o contraseña incorrectos.');
        this.cargando.set(false);
      }
    });
  }
}
