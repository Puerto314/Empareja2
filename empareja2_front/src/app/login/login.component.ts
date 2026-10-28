import { HttpErrorResponse } from '@angular/common/http';
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
    const correo = this.email.trim();
    // No se exige "@": el backend también acepta un usuario simple (ej. el administrador).
    if (!correo || !this.password) {
      this.error.set('Escribe tu correo y tu contraseña para entrar.');
      return;
    }
    this.error.set('');
    this.cargando.set(true);
    this.authService.iniciarSesion(correo, this.password).subscribe({
      next: respuesta => {
        this.authService.guardarToken(respuesta.token);
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: (err: unknown) => {
        this.error.set(this.mensajeDeError(err));
        this.cargando.set(false);
      }
    });
  }

  private mensajeDeError(err: unknown): string {
    if (err instanceof HttpErrorResponse) {
      if (err.status === 401) { return 'Correo o contraseña incorrectos.'; }
      if (err.status === 0) { return 'No se pudo conectar con el servidor. Intenta de nuevo en un momento.'; }
    }
    return 'No pudimos iniciar sesión. Intenta de nuevo.';
  }
}
