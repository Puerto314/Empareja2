import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { API_URL } from '../config/api.config';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private authService = inject(AuthService);
  private router = inject(Router);

  intercept(req: HttpRequest<unknown>, next: HttpHandler) {
    // El token solo se envía al backend propio, nunca a otros dominios.
    const esApi = req.url.startsWith(API_URL);
    const esAuth = req.url.startsWith(`${API_URL}/auth/`);
    const token = this.authService.obtenerToken();
    const peticion = esApi && token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(peticion).pipe(
      catchError(err => {
        // 401 fuera del login = token vencido o inválido: se cierra la sesión y se vuelve al login.
        if (err instanceof HttpErrorResponse && err.status === 401 && esApi && !esAuth) {
          this.authService.cerrarSesion();
          this.router.navigate(['/login']);
        }
        return throwError(() => err);
      })
    );
  }
}
