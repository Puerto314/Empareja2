import { HttpErrorResponse } from '@angular/common/http';
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
<<<<<<< HEAD
    const correo = this.email.trim();
    // No se exige "@": el backend también acepta un usuario simple (ej. el administrador).
    if (!correo || !this.password) {
=======
    if (!this.correo.trim() || !this.contrasena) {
>>>>>>> main
      this.error.set('Escribe tu correo y tu contraseña para entrar.');
      return;
    }
    this.error.set('');
    this.cargando.set(true);
<<<<<<< HEAD
    this.authService.iniciarSesion(correo, this.password).subscribe({
      next: respuesta => {
        this.authService.guardarToken(respuesta.token);
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: (err: unknown) => {
        this.error.set(this.mensajeDeError(err));
=======
    this.authService.iniciarSesion({ correo: this.correo.trim(), contrasena: this.contrasena }).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: err => {
        this.error.set(err?.status === 401 ? 'Correo o contraseña incorrectos.' : mensajeDeError(err, 'No pudimos iniciar sesión.'));
>>>>>>> main
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
