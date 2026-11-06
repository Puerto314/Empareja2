import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_URL } from '../config/api.config';
import { ActualizarPerfilRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class CuentaService {
  private http = inject(HttpClient);
  private auth = inject(AuthService);

  miPerfil(): Observable<PerfilModel> {
    return this.http.get<PerfilModel>(`${API_URL}/usuarios/me`).pipe(tap(p => this.auth.actualizarUsuario(p)));
  }

  actualizar(datos: ActualizarPerfilRequestModel): Observable<PerfilModel> {
    return this.http.put<PerfilModel>(`${API_URL}/usuarios/me`, datos).pipe(tap(p => this.auth.actualizarUsuario(p)));
  }

  eliminar(): Observable<void> {
    return this.http.delete<void>(`${API_URL}/usuarios/me`).pipe(tap(() => this.auth.cerrarSesion()));
  }
}
