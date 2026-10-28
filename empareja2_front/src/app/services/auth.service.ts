import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_URL } from '../config/api.config';
import { AuthResponseModel } from '../models/auth-response.model';
import { LoginRequestModel } from '../models/login-request.model';
import { RegistroRequestModel } from '../models/registro-request.model';
import { UsuarioPerfilModel } from '../models/usuario-perfil.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private url = `${API_URL}/auth`;

  iniciarSesion(datos: LoginRequestModel): Observable<AuthResponseModel> {
    return this.http.post<AuthResponseModel>(`${this.url}/login`, datos).pipe(tap(r => this.guardarSesion(r)));
  }

  registrar(datos: RegistroRequestModel): Observable<AuthResponseModel> {
    return this.http.post<AuthResponseModel>(`${this.url}/registro`, datos).pipe(tap(r => this.guardarSesion(r)));
  }

  guardarSesion(r: AuthResponseModel): void {
    localStorage.setItem('token', r.token);
    localStorage.setItem('usuario', JSON.stringify(r.usuario));
    localStorage.setItem('expira', String(Date.now() + r.expiresInMs));
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  obtenerUsuario(): UsuarioPerfilModel | null {
    try {
      return JSON.parse(localStorage.getItem('usuario') ?? 'null');
    } catch {
      return null;
    }
  }

  cerrarSesion(): void {
    ['token', 'usuario', 'expira'].forEach(k => localStorage.removeItem(k));
  }

  estaLogueado(): boolean {
    return !!this.obtenerToken() && Date.now() < Number(localStorage.getItem('expira') ?? 0);
  }
}
