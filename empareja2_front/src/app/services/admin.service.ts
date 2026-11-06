import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import {
  CarreraModel, CarreraRequestModel, CiudadRequestModel, OfertaModel, OfertaRequestModel,
  UniversidadRequestModel
} from '../models/catalogo.model';
import { EstadoReporteEnum } from '../models/estado-reporte.enum';
import { ComentarioModel, ReaccionAdminModel, ReporteModel } from '../models/interaccion.model';
import { AdminUsuarioRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { CiudadModel, UniversidadModel } from '../models/universidad.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private http = inject(HttpClient);
  private base = `${API_URL}/admin`;

  listarUsuarios(): Observable<PerfilModel[]> { return this.http.get<PerfilModel[]>(`${this.base}/usuarios`); }
  obtenerUsuario(id: number): Observable<PerfilModel> { return this.http.get<PerfilModel>(`${this.base}/usuarios/${id}`); }
  crearUsuario(d: AdminUsuarioRequestModel): Observable<PerfilModel> { return this.http.post<PerfilModel>(`${this.base}/usuarios`, d); }
  actualizarUsuario(id: number, d: AdminUsuarioRequestModel): Observable<PerfilModel> { return this.http.put<PerfilModel>(`${this.base}/usuarios/${id}`, d); }
  eliminarUsuario(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/usuarios/${id}`); }

  listarReportes(estado?: EstadoReporteEnum): Observable<ReporteModel[]> {
    return this.http.get<ReporteModel[]>(`${this.base}/reportes`, estado ? { params: { estado } } : {});
  }
  obtenerReporte(id: number): Observable<ReporteModel> { return this.http.get<ReporteModel>(`${this.base}/reportes/${id}`); }
  actualizarReporte(id: number, estado: EstadoReporteEnum): Observable<ReporteModel> {
    return this.http.put<ReporteModel>(`${this.base}/reportes/${id}`, { estado });
  }
  eliminarReporte(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/reportes/${id}`); }

  listarComentarios(): Observable<ComentarioModel[]> { return this.http.get<ComentarioModel[]>(`${this.base}/comentarios`); }
  eliminarComentario(idAutor: number, numero: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/comentarios/${idAutor}/${numero}`);
  }

  listarMeGusta(): Observable<ReaccionAdminModel[]> { return this.http.get<ReaccionAdminModel[]>(`${this.base}/me-gusta`); }
  eliminarMeGusta(idAutor: number, numero: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/me-gusta/${idAutor}/${numero}`);
  }

  crearCiudad(d: CiudadRequestModel): Observable<CiudadModel> { return this.http.post<CiudadModel>(`${this.base}/catalogos/ciudades`, d); }
  actualizarCiudad(id: number, d: CiudadRequestModel): Observable<CiudadModel> { return this.http.put<CiudadModel>(`${this.base}/catalogos/ciudades/${id}`, d); }
  eliminarCiudad(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/catalogos/ciudades/${id}`); }

  crearUniversidad(d: UniversidadRequestModel): Observable<UniversidadModel> { return this.http.post<UniversidadModel>(`${this.base}/catalogos/universidades`, d); }
  actualizarUniversidad(id: number, d: UniversidadRequestModel): Observable<UniversidadModel> { return this.http.put<UniversidadModel>(`${this.base}/catalogos/universidades/${id}`, d); }
  eliminarUniversidad(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/catalogos/universidades/${id}`); }

  crearCarrera(d: CarreraRequestModel): Observable<CarreraModel> { return this.http.post<CarreraModel>(`${this.base}/catalogos/carreras`, d); }
  actualizarCarrera(id: number, d: CarreraRequestModel): Observable<CarreraModel> { return this.http.put<CarreraModel>(`${this.base}/catalogos/carreras/${id}`, d); }
  eliminarCarrera(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/catalogos/carreras/${id}`); }

  crearOferta(d: OfertaRequestModel): Observable<OfertaModel> { return this.http.post<OfertaModel>(`${this.base}/catalogos/oferta`, d); }
  actualizarOferta(idCarrera: number, idUniversidad: number, facultad: string): Observable<OfertaModel> {
    return this.http.put<OfertaModel>(`${this.base}/catalogos/oferta/${idCarrera}/${idUniversidad}`, { facultad });
  }
  eliminarOferta(idCarrera: number, idUniversidad: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/catalogos/oferta/${idCarrera}/${idUniversidad}`);
  }
}
