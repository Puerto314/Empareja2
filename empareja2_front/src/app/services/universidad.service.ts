import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, forkJoin, map, of, throwError } from 'rxjs';
import { API_URL } from '../config/api.config';
import { ResumenUniversidadesModel, UniversidadModel } from '../models/universidad.model';
import { EstudianteService } from './estudiante.service';

@Injectable({ providedIn: 'root' })
export class UniversidadService {
  private http = inject(HttpClient);
  private estudianteService = inject(EstudianteService);

  // GET /api/catalogos/universidades (público). Con "nombre" busca por nombre o sigla.
  listar(nombre?: string): Observable<UniversidadModel[]> {
    return this.http.get<UniversidadModel[]>(`${API_URL}/catalogos/universidades`,
      nombre ? { params: { nombre } } : {});
  }

  // GET /api/catalogos/universidades/{id}. Devuelve undefined si no existe (404) o el id no es válido (400).
  obtener(id: number): Observable<UniversidadModel | undefined> {
    return this.http.get<UniversidadModel>(`${API_URL}/catalogos/universidades/${id}`).pipe(
      catchError((err: unknown) =>
        err instanceof HttpErrorResponse && (err.status === 404 || err.status === 400)
          ? of(undefined)
          : throwError(() => err))
    );
  }

  // Universidades con su cantidad de estudiantes y una vista previa de 4, ordenadas por nombre.
  // Incluye las universidades sin estudiantes (cantidad 0).
  listarResumen(): Observable<ResumenUniversidadesModel> {
    return forkJoin({ unis: this.listar(), estudiantes: this.estudianteService.listarTodos() }).pipe(
      map(({ unis, estudiantes }) => ({
        totalEstudiantes: new Set(estudiantes.map(e => e.id)).size,
        universidades: [...unis]
          .sort((a, b) => a.nombre.localeCompare(b.nombre))
          .map(u => {
            const alumnos = estudiantes.filter(e => e.estudios.some(x => x.idUniversidad === u.idUniversidad));
            return { ...u, cantidad: alumnos.length, vistaPrevia: alumnos.slice(0, 4) };
          })
      })));
  }
}
