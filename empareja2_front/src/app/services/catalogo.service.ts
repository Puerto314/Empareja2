import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { CarreraModel, OfertaModel } from '../models/catalogo.model';
import { CiudadModel } from '../models/universidad.model';

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private http = inject(HttpClient);

  ciudades(): Observable<CiudadModel[]> { return this.http.get<CiudadModel[]>(`${API_URL}/catalogos/ciudades`); }
  ciudad(id: number): Observable<CiudadModel> { return this.http.get<CiudadModel>(`${API_URL}/catalogos/ciudades/${id}`); }

  carreras(): Observable<CarreraModel[]> { return this.http.get<CarreraModel[]>(`${API_URL}/catalogos/carreras`); }
  carrera(id: number): Observable<CarreraModel> { return this.http.get<CarreraModel>(`${API_URL}/catalogos/carreras/${id}`); }

  oferta(idUniversidad?: number): Observable<OfertaModel[]> {
    return this.http.get<OfertaModel[]>(`${API_URL}/catalogos/oferta`,
      idUniversidad === undefined ? {} : { params: { universidadId: idUniversidad } });
  }
}
