import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { AuthResponseModel } from '../models/auth-response.model';
import { LoginRequestModel } from '../models/login-request.model';
import { RegistroRequestModel } from '../models/registro-request.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  // POST /api/auth/login -> { token, tokenType, expiresInMs, usuario }
  iniciarSesion(correo: string, contrasena: string): Observable<AuthResponseModel> {
    const cuerpo: LoginRequestModel = { correo, contrasena };
    return this.http.post<AuthResponseModel>(`${API_URL}/auth/login`, cuerpo);
  }

  // POST /api/auth/registro (el backend responde 201 con el mismo formato del login)
  registrar(datos: RegistroRequestModel): Observable<AuthResponseModel> {
    return this.http.post<AuthResponseModel>(`${API_URL}/auth/registro`, datos);
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
