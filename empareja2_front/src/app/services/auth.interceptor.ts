<<<<<<< HEAD
import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { API_URL } from '../config/api.config';
import { AuthService } from './auth.service';
=======
import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap, throwError } from 'rxjs';
>>>>>>> main

// Lee el token directo de localStorage (sin AuthService) para evitar dependencias circulares con HttpClient.
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
<<<<<<< HEAD
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
=======
  private router = inject(Router);

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');
    const solicitud = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(solicitud).pipe(
      // REGISTRO PARA PRUEBAS: muestra en la consola del navegador (F12) cada llamada al backend.
      // Borrar este tap cuando ya no se necesite.
      tap({
        next: e => { if (e instanceof HttpResponse) console.log(`[API] ${req.method} ${req.urlWithParams} -> ${e.status}`); },
        error: (err: HttpErrorResponse) => console.warn(`[API] ${req.method} ${req.urlWithParams} -> ERROR ${err.status}`, err.error)
      })

      // PRUEBA C (token rechazado): para que el front cierre la sesión y vuelva al login cuando el backend
      // responde 401 con un token guardado, agrega una coma después del tap de arriba y descomenta este bloque.
      // , catchError((err: HttpErrorResponse) => {
      //   if (err.status === 401 && token) {
      //     ['token', 'usuario', 'expira'].forEach(k => localStorage.removeItem(k));
      //     this.router.navigate(['/login']);
      //   }
      //   return throwError(() => err);
      // })
>>>>>>> main
    );
  }
}
