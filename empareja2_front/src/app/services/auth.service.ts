import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  // Sin conexión aún. Después: POST /api/auth/login con { email, password } y devolver el token JWT.
  iniciarSesion(email: string, password: string): Observable<string> {
    return of('token-demo');
  }

  guardarToken(token: string): void {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
  }

  estaLogueado(): boolean {
    return !!this.obtenerToken();
  }
}
