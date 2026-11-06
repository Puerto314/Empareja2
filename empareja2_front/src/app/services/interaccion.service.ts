import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { ComentarioModel, MeGustaModel, ReaccionModel, ReporteModel, ReporteRequestModel } from '../models/interaccion.model';

@Injectable({ providedIn: 'root' })
export class InteraccionService {
  private http = inject(HttpClient);

  darMeGusta(idUsuario: number): Observable<MeGustaModel> {
    return this.http.post<MeGustaModel>(`${API_URL}/usuarios/${idUsuario}/me-gusta`, {});
  }

  quitarMeGusta(idUsuario: number): Observable<MeGustaModel> {
    return this.http.delete<MeGustaModel>(`${API_URL}/usuarios/${idUsuario}/me-gusta`);
  }

  listarMeGusta(idUsuario: number): Observable<ReaccionModel[]> {
    return this.http.get<ReaccionModel[]>(`${API_URL}/usuarios/${idUsuario}/me-gusta`);
  }

  enviarComentario(idDestinatario: number, contenido: string): Observable<ComentarioModel> {
    return this.http.post<ComentarioModel>(`${API_URL}/usuarios/${idDestinatario}/comentarios`, { contenido });
  }

  comentariosRecibidos(): Observable<ComentarioModel[]> {
    return this.http.get<ComentarioModel[]>(`${API_URL}/usuarios/me/comentarios/recibidos`);
  }

  comentariosEnviados(): Observable<ComentarioModel[]> {
    return this.http.get<ComentarioModel[]>(`${API_URL}/usuarios/me/comentarios/enviados`);
  }

  reportar(idReportado: number, datos: ReporteRequestModel): Observable<ReporteModel> {
    return this.http.post<ReporteModel>(`${API_URL}/usuarios/${idReportado}/reportes`, datos);
  }
}
