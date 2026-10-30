import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { CarreraModel, OfertaModel, RedSocialModel } from '../models/catalogo.model';
import { CiudadModel } from '../models/universidad.model';

// Catálogos públicos de referencia: /api/catalogos/** (las universidades están en UniversidadService).
@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private http = inject(HttpClient);

  ciudades(): Observable<CiudadModel[]> { return this.http.get<CiudadModel[]>(`${API_URL}/catalogos/ciudades`); }
  ciudad(id: number): Observable<CiudadModel> { return this.http.get<CiudadModel>(`${API_URL}/catalogos/ciudades/${id}`); }

  carreras(): Observable<CarreraModel[]> { return this.http.get<CarreraModel[]>(`${API_URL}/catalogos/carreras`); }
  carrera(id: number): Observable<CarreraModel> { return this.http.get<CarreraModel>(`${API_URL}/catalogos/carreras/${id}`); }

  redesSociales(): Observable<RedSocialModel[]> { return this.http.get<RedSocialModel[]>(`${API_URL}/catalogos/redes-sociales`); }
  redSocial(id: number): Observable<RedSocialModel> { return this.http.get<RedSocialModel>(`${API_URL}/catalogos/redes-sociales/${id}`); }

  // GET /api/catalogos/oferta[?universidadId=N]: qué carreras ofrece cada universidad
  oferta(idUniversidad?: number): Observable<OfertaModel[]> {
    return this.http.get<OfertaModel[]>(`${API_URL}/catalogos/oferta`,
      idUniversidad === undefined ? {} : { params: { universidadId: idUniversidad } });
  }
}
