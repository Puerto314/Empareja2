import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { AuthResponseModel } from '../models/auth-response.model';
import { LoginRequestModel } from '../models/login-request.model';
import { PerfilModel } from '../models/perfil.model';
import { RegistroRequestModel } from '../models/registro-request.model';
import { RolEnum } from '../models/rol.enum';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  iniciarSesion(correo: string, contrasena: string): Observable<AuthResponseModel> {
    const cuerpo: LoginRequestModel = { correo, contrasena };
    return this.http.post<AuthResponseModel>(`${API_URL}/auth/login`, cuerpo);
  }

  registrar(datos: RegistroRequestModel): Observable<AuthResponseModel> {
    return this.http.post<AuthResponseModel>(`${API_URL}/auth/registro`, datos);
  }

  guardarToken(token: string): void {
    localStorage.setItem('token', token);
  }

  guardarSesion(respuesta: AuthResponseModel): void {
    this.guardarToken(respuesta.token);
    if (respuesta.usuario) {
      localStorage.setItem('usuario', JSON.stringify(respuesta.usuario));
    }
  }

  obtenerUsuario(): PerfilModel | null {
    try {
      const texto = localStorage.getItem('usuario');
      return texto ? (JSON.parse(texto) as PerfilModel) : null;
    } catch {
      return null;
    }
  }

  actualizarUsuario(usuario: PerfilModel): void {
    localStorage.setItem('usuario', JSON.stringify(usuario));
  }

  esAdmin(): boolean {
    return this.obtenerUsuario()?.rol === RolEnum.ADMIN;
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  estaLogueado(): boolean {
    return !!this.obtenerToken();
  }
}
