import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { RolEnum } from '../models/rol.enum';
import { AuthService } from '../services/auth.service';
import { mensajeDeError as mensajeDeErrorHttp } from '../utils/error.util';

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
    if (!correo || !this.password) {
      this.error.set('Escribe tu correo y tu contraseña para entrar.');
      return;
    }
    this.error.set('');
    this.cargando.set(true);
    this.authService.iniciarSesion(correo, this.password).subscribe({
      next: respuesta => {
        this.authService.guardarSesion(respuesta);
        this.cargando.set(false);
        this.router.navigate([respuesta.usuario?.rol === RolEnum.ADMIN ? '/admin' : '/universidades']);
      },
      error: (err: unknown) => {
        this.error.set(this.mensajeDeError(err));
        this.cargando.set(false);
      }
    });
  }

  private mensajeDeError(err: unknown): string {
    const porDefecto = err instanceof HttpErrorResponse && err.status === 401
      ? 'Correo o contraseña incorrectos.'
      : 'No pudimos iniciar sesión. Intenta de nuevo.';
    return mensajeDeErrorHttp(err, porDefecto);
  }
}
