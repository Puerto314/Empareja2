import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { ComentarioModel, MeGustaModel, ReaccionModel, ReporteModel, ReporteRequestModel } from '../models/interaccion.model';

// Me gusta, comentarios privados y reportes entre usuarios.
@Injectable({ providedIn: 'root' })
export class InteraccionService {
  private http = inject(HttpClient);

  // POST /api/usuarios/{id}/me-gusta (201)
  darMeGusta(idUsuario: number): Observable<MeGustaModel> {
    return this.http.post<MeGustaModel>(`${API_URL}/usuarios/${idUsuario}/me-gusta`, {});
  }

  // DELETE /api/usuarios/{id}/me-gusta
  quitarMeGusta(idUsuario: number): Observable<MeGustaModel> {
    return this.http.delete<MeGustaModel>(`${API_URL}/usuarios/${idUsuario}/me-gusta`);
  }

  // GET /api/usuarios/{id}/me-gusta: quiénes dieron me gusta (público)
  listarMeGusta(idUsuario: number): Observable<ReaccionModel[]> {
    return this.http.get<ReaccionModel[]>(`${API_URL}/usuarios/${idUsuario}/me-gusta`);
  }

  // POST /api/usuarios/{id}/comentarios (201): comentario privado
  enviarComentario(idDestinatario: number, contenido: string): Observable<ComentarioModel> {
    return this.http.post<ComentarioModel>(`${API_URL}/usuarios/${idDestinatario}/comentarios`, { contenido });
  }

  // GET /api/usuarios/me/comentarios/recibidos
  comentariosRecibidos(): Observable<ComentarioModel[]> {
    return this.http.get<ComentarioModel[]>(`${API_URL}/usuarios/me/comentarios/recibidos`);
  }

  // GET /api/usuarios/me/comentarios/enviados
  comentariosEnviados(): Observable<ComentarioModel[]> {
    return this.http.get<ComentarioModel[]>(`${API_URL}/usuarios/me/comentarios/enviados`);
  }

  // POST /api/usuarios/{id}/reportes (201)
  reportar(idReportado: number, datos: ReporteRequestModel): Observable<ReporteModel> {
    return this.http.post<ReporteModel>(`${API_URL}/usuarios/${idReportado}/reportes`, datos);
  }
}
