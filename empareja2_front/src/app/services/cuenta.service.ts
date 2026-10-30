import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_URL } from '../config/api.config';
import { ActualizarPerfilRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { AuthService } from './auth.service';

// Perfil propio del usuario autenticado: /api/usuarios/me
@Injectable({ providedIn: 'root' })
export class CuentaService {
  private http = inject(HttpClient);
  private auth = inject(AuthService);

  // GET /api/usuarios/me
  miPerfil(): Observable<PerfilModel> {
    return this.http.get<PerfilModel>(`${API_URL}/usuarios/me`).pipe(tap(p => this.auth.actualizarUsuario(p)));
  }

  // PUT /api/usuarios/me (las listas reemplazan a las actuales)
  actualizar(datos: ActualizarPerfilRequestModel): Observable<PerfilModel> {
    return this.http.put<PerfilModel>(`${API_URL}/usuarios/me`, datos).pipe(tap(p => this.auth.actualizarUsuario(p)));
  }

  // DELETE /api/usuarios/me (204) y cierra la sesión local
  eliminar(): Observable<void> {
    return this.http.delete<void>(`${API_URL}/usuarios/me`).pipe(tap(() => this.auth.cerrarSesion()));
  }
}
