import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { API_URL } from '../config/api.config';
import { AuthService } from './auth.service';
import { RegistroErroresService } from './registro-errores.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private authService = inject(AuthService);
  private router = inject(Router);
  private registro = inject(RegistroErroresService);

  intercept(req: HttpRequest<unknown>, next: HttpHandler) {
    const esApi = req.url.startsWith(API_URL);
    const esAuth = req.url.startsWith(`${API_URL}/auth/`);
    const token = this.authService.obtenerToken();
    const peticion = esApi && token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(peticion).pipe(
      catchError(err => {
        if (err instanceof HttpErrorResponse) { this.registro.http(err, req.method, req.url); }
        if (err instanceof HttpErrorResponse && err.status === 401 && esApi && !esAuth) {
          this.authService.cerrarSesion();
          this.router.navigate(['/login']);
        }
        return throwError(() => err);
      })
    );
  }
}
