import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { mensajeDeError } from '../utils/error.util';

@Component({
  standalone: false,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  correo = '';
  contrasena = '';
  error = signal('');
  cargando = signal(false);

  entrar(): void {
    if (!this.correo.trim() || !this.contrasena) {
      this.error.set('Escribe tu correo y tu contraseña para entrar.');
      return;
    }
    this.error.set('');
    this.cargando.set(true);
    this.authService.iniciarSesion({ correo: this.correo.trim(), contrasena: this.contrasena }).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: err => {
        this.error.set(err?.status === 401 ? 'Correo o contraseña incorrectos.' : mensajeDeError(err, 'No pudimos iniciar sesión.'));
        this.cargando.set(false);
      }
    });
  }
}
