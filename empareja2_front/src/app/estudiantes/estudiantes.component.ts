import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
<<<<<<< HEAD
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { EstudianteModel } from '../models/estudiante.model';
import { EstudioModel } from '../models/estudio.model';
import { UniversidadModel } from '../models/universidad.model';
import { EstudianteService } from '../services/estudiante.service';
import { UniversidadService } from '../services/universidad.service';
import { gradient, hueOf, initials, siglaCorta } from '../utils/avatar.util';

interface Datos { universidad?: UniversidadModel; alumnos: EstudianteModel[]; fallo?: boolean }
=======
import { catchError, forkJoin, map, of, switchMap, tap } from 'rxjs';
import { CatalogoService } from '../services/catalogo.service';
import { UsuarioService } from '../services/usuario.service';
import { UniversidadModel } from '../models/universidad.model';
import { UsuarioPublicoModel } from '../models/usuario-publico.model';
import { fondoAvatar, gradient, hueOf, inicialesAvatar } from '../utils/avatar.util';
import { mensajeDeError } from '../utils/error.util';
>>>>>>> main

@Component({
  standalone: false,
  selector: 'app-estudiantes',
  templateUrl: './estudiantes.component.html',
  styleUrls: ['./estudiantes.component.css']
})
export class EstudiantesComponent {
  private route = inject(ActivatedRoute);
<<<<<<< HEAD
  private estudianteService = inject(EstudianteService);
  private universidadService = inject(UniversidadService);

  // La ruta /universidades/:id trae el idUniversidad (no el nombre).
  private idUni = signal(0);
  universidad = signal<UniversidadModel | undefined>(undefined);
  todos = signal<EstudianteModel[]>([]);
  cargando = signal(true);
  error = signal('');
  uni = computed(() => this.universidad()?.nombre ?? '');
  sigla = computed(() => { const u = this.universidad(); return u ? siglaCorta(u.sigla, u.nombre) : ''; });
=======
  private catalogoService = inject(CatalogoService);
  private usuarioService = inject(UsuarioService);

  idUniversidad = signal(0);
  universidad = signal<UniversidadModel | undefined>(undefined);
  todos = signal<UsuarioPublicoModel[]>([]);
  cargando = signal(true);
  error = signal('');
  nombre = computed(() => this.universidad()?.nombre ?? 'Universidad');
>>>>>>> main

  q = signal('');
  rango = signal<'todos' | 'a' | 'b' | 'c'>('todos');
  filtros = [
    { id: 'todos', label: 'Todos' }, { id: 'a', label: 'Semestres 1-3' },
    { id: 'b', label: 'Semestres 4-6' }, { id: 'c', label: 'Semestre 7 o más' }
  ] as const;
  grad = gradient; hue = hueOf; fondo = fondoAvatar; ini = inicialesAvatar;

  constructor() {
    this.route.paramMap.pipe(
      map(p => Number(p.get('id'))),
      tap(id => { this.idUniversidad.set(id); this.cargando.set(true); this.error.set(''); }),
      switchMap(id => forkJoin({
        universidades: this.catalogoService.universidades(),
        usuarios: this.usuarioService.listarCatalogo(id)
      }).pipe(catchError(err => {
        this.error.set(mensajeDeError(err, 'No pudimos cargar a los estudiantes.'));
        return of(null);
      }))),
      takeUntilDestroyed()
    ).subscribe(r => {
      this.cargando.set(false);
      if (r) {
        this.universidad.set(r.universidades.find(u => u.idUniversidad === this.idUniversidad()));
        this.todos.set(r.usuarios);
      }
    });
  }

  // Si estudia en varias universidades, se muestra el estudio de la universidad que se está viendo.
  estudioEn(u: UsuarioPublicoModel) {
    return u.estudios.find(e => e.idUniversidad === this.idUniversidad()) ?? u.estudios[0];
  }

  // Carrera y semestre que el estudiante cursa en ESTA universidad (puede tener varios estudios).
  estudioEn(s: EstudianteModel): EstudioModel | undefined {
    return s.estudios.find(e => e.idUniversidad === this.idUni());
  }

  list = computed(() => {
    const t = this.q().toLowerCase(), r = this.rango();
    return this.todos().filter(s => {
<<<<<<< HEAD
      const e = this.estudioEn(s);
      const sem = e?.semestre ?? 0;
      return (s.nombreCompleto + ' ' + (e?.carrera ?? '')).toLowerCase().includes(t) &&
=======
      const est = this.estudioEn(s);
      const sem = est?.semestre ?? 0;
      return (s.nombreCompleto + ' ' + (est?.carrera ?? '')).toLowerCase().includes(t) &&
>>>>>>> main
        (r === 'todos' || (r === 'a' && sem <= 3) || (r === 'b' && sem >= 4 && sem <= 6) || (r === 'c' && sem >= 7));
    });
  });

  constructor() {
    this.route.paramMap.pipe(
      map(p => Number(p.get('id'))),
      tap(id => { this.idUni.set(id); this.cargando.set(true); this.error.set(''); }),
      switchMap(id => this.universidadService.obtener(id).pipe(
        switchMap(universidad => universidad
          ? this.estudianteService.listarPorUniversidad(id).pipe(map((alumnos): Datos => ({ universidad, alumnos })))
          : of<Datos>({ alumnos: [] })),
        catchError(() => of<Datos>({ alumnos: [], fallo: true })))),
      takeUntilDestroyed()
    ).subscribe(datos => {
      this.universidad.set(datos.universidad);
      this.todos.set(datos.alumnos);
      this.error.set(datos.fallo ? 'No pudimos cargar los estudiantes. Revisa que el servidor esté encendido e intenta de nuevo.' : '');
      this.cargando.set(false);
    });
  }
}
