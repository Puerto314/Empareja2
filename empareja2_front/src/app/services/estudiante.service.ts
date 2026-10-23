import { Injectable } from '@angular/core';
import { Observable, map, of, switchMap } from 'rxjs';
import { EstudianteModel } from '../models/estudiante.model';
import { UniversidadModel } from '../models/universidad.model';
import { ESTUDIANTES_MOCK } from '../mocks/estudiantes.mock';
import { siglaDe } from '../utils/avatar.util';

@Injectable({ providedIn: 'root' })
export class EstudianteService {

  // Sin conexión aún. Después: GET /api/students
  listarTodos(): Observable<EstudianteModel[]> {
    return of(ESTUDIANTES_MOCK);
  }

  // Sin conexión aún. Después: GET /api/students/{id}
  obtenerPorId(id: number): Observable<EstudianteModel | undefined> {
    return of(ESTUDIANTES_MOCK.find(e => e.id === id));
  }

  listarPorUniversidad(universidad: string): Observable<EstudianteModel[]> {
    return this.listarTodos().pipe(map(lista => lista.filter(e => e.university === universidad)));
  }

  listarUniversidades(): Observable<UniversidadModel[]> {
    return this.listarTodos().pipe(map(lista =>
      Array.from(new Set(lista.map(e => e.university)))
        .sort((a, b) => a.localeCompare(b))
        .map(nombre => {
          const alumnos = lista.filter(e => e.university === nombre);
          return { nombre, sigla: siglaDe(nombre), cantidad: alumnos.length, vistaPrevia: alumnos.slice(0, 4) };
        })));
  }

  listarCompaneros(id: number, limite = 4): Observable<EstudianteModel[]> {
    return this.obtenerPorId(id).pipe(
      switchMap(est => est ? this.listarPorUniversidad(est.university) : of([] as EstudianteModel[])),
      map(lista => lista.filter(e => e.id !== id).slice(0, limite)));
  }
}
