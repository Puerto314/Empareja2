import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api.config';
import { CarreraModel } from '../models/carrera.model';
import { UniversidadModel } from '../models/universidad.model';

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private http = inject(HttpClient);
  private url = `${API_URL}/catalogos`;

  universidades(): Observable<UniversidadModel[]> {
    return this.http.get<UniversidadModel[]>(`${this.url}/universidades`);
  }

  carreras(): Observable<CarreraModel[]> {
    return this.http.get<CarreraModel[]>(`${this.url}/carreras`);
  }
}
