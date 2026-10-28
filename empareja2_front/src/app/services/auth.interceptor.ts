import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap, throwError } from 'rxjs';

// Lee el token directo de localStorage (sin AuthService) para evitar dependencias circulares con HttpClient.
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
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
    );
  }
}
