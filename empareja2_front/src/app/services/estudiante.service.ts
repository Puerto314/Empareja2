import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, throwError } from 'rxjs';
import { API_URL } from '../config/api.config';
import { EstudianteModel } from '../models/estudiante.model';

@Injectable({ providedIn: 'root' })
export class EstudianteService {
  private http = inject(HttpClient);

  // GET /api/usuarios -> todos los estudiantes menos el usuario autenticado (solo info pública)
  listarTodos(): Observable<EstudianteModel[]> {
    return this.http.get<EstudianteModel[]>(`${API_URL}/usuarios`);
  }

  // GET /api/usuarios?universidadId={id}
  listarPorUniversidad(idUniversidad: number): Observable<EstudianteModel[]> {
    return this.http.get<EstudianteModel[]>(`${API_URL}/usuarios`, { params: { universidadId: idUniversidad } });
  }

  // GET /api/usuarios/ranking[?universidadId=N&limite=20]: usuarios con más "me gusta", de mayor a menor.
  ranking(idUniversidad?: number, limite = 20): Observable<EstudianteModel[]> {
    const params: Record<string, number> = { limite };
    if (idUniversidad !== undefined) { params['universidadId'] = idUniversidad; }
    return this.http.get<EstudianteModel[]>(`${API_URL}/usuarios/ranking`, { params });
  }

  // GET /api/usuarios/{id}. Devuelve undefined si no existe (404) o el id no es válido (400).
  obtenerPorId(id: number): Observable<EstudianteModel | undefined> {
    return this.http.get<EstudianteModel>(`${API_URL}/usuarios/${id}`).pipe(
      catchError((err: unknown) =>
        err instanceof HttpErrorResponse && (err.status === 404 || err.status === 400)
          ? of(undefined)
          : throwError(() => err))
    );
  }

  // Otros estudiantes de la primera universidad del estudiante, sin incluirlo a él.
  listarCompaneros(estudiante: EstudianteModel, limite = 4): Observable<EstudianteModel[]> {
    const idUniversidad = estudiante.estudios[0]?.idUniversidad;
    if (idUniversidad === undefined) {
      return of([]);
    }
    return this.listarPorUniversidad(idUniversidad).pipe(
      map(lista => lista.filter(e => e.id !== estudiante.id).slice(0, limite)));
  }
}
