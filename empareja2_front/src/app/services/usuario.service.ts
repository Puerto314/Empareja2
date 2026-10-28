import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { MeGustaModel } from '../models/me-gusta.model';
import { UsuarioPublicoModel } from '../models/usuario-publico.model';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);
  private url = `${API_URL}/usuarios`;

  // Sin id devuelve todos los estudiantes; con id, solo los de esa universidad.
  listarCatalogo(idUniversidad?: number): Observable<UsuarioPublicoModel[]> {
    let params = new HttpParams();
    if (idUniversidad !== undefined) params = params.set('universidadId', idUniversidad);
    return this.http.get<UsuarioPublicoModel[]>(this.url, { params });
  }

  obtenerPublico(id: number): Observable<UsuarioPublicoModel> {
    return this.http.get<UsuarioPublicoModel>(`${this.url}/${id}`);
  }

  darMeGusta(id: number): Observable<MeGustaModel> {
    return this.http.post<MeGustaModel>(`${this.url}/${id}/me-gusta`, {});
  }

  quitarMeGusta(id: number): Observable<MeGustaModel> {
    return this.http.delete<MeGustaModel>(`${this.url}/${id}/me-gusta`);
  }
}
