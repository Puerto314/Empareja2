import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, throwError } from 'rxjs';
import { API_URL } from '../config/api.config';
import { EstudianteModel } from '../models/estudiante.model';

@Injectable({ providedIn: 'root' })
export class EstudianteService {
  private http = inject(HttpClient);

  listarTodos(): Observable<EstudianteModel[]> {
    return this.http.get<EstudianteModel[]>(`${API_URL}/usuarios`);
  }

  listarPorUniversidad(idUniversidad: number): Observable<EstudianteModel[]> {
    return this.http.get<EstudianteModel[]>(`${API_URL}/usuarios`, { params: { universidadId: idUniversidad } });
  }

  ranking(idUniversidad?: number, limite = 20): Observable<EstudianteModel[]> {
    const params: Record<string, number> = { limite };
    if (idUniversidad !== undefined) { params['universidadId'] = idUniversidad; }
    return this.http.get<EstudianteModel[]>(`${API_URL}/usuarios/ranking`, { params });
  }

  obtenerPorId(id: number): Observable<EstudianteModel | undefined> {
    return this.http.get<EstudianteModel>(`${API_URL}/usuarios/${id}`).pipe(
      catchError((err: unknown) =>
        err instanceof HttpErrorResponse && (err.status === 404 || err.status === 400)
          ? of(undefined)
          : throwError(() => err))
    );
  }

  listarCompaneros(estudiante: EstudianteModel, limite = 4): Observable<EstudianteModel[]> {
    const idUniversidad = estudiante.estudios[0]?.idUniversidad;
    if (idUniversidad === undefined) {
      return of([]);
    }
    return this.listarPorUniversidad(idUniversidad).pipe(
      map(lista => lista.filter(e => e.id !== estudiante.id).slice(0, limite)));
  }
}
