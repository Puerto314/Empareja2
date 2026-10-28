import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { catchError, forkJoin, map, of, switchMap, tap } from 'rxjs';
import { CatalogoService } from '../services/catalogo.service';
import { UsuarioService } from '../services/usuario.service';
import { UniversidadModel } from '../models/universidad.model';
import { UsuarioPublicoModel } from '../models/usuario-publico.model';
import { fondoAvatar, gradient, hueOf, inicialesAvatar } from '../utils/avatar.util';
import { mensajeDeError } from '../utils/error.util';

@Component({
  standalone: false,
  selector: 'app-estudiantes',
  templateUrl: './estudiantes.component.html',
  styleUrls: ['./estudiantes.component.css']
})
export class EstudiantesComponent {
  private route = inject(ActivatedRoute);
  private catalogoService = inject(CatalogoService);
  private usuarioService = inject(UsuarioService);

  idUniversidad = signal(0);
  universidad = signal<UniversidadModel | undefined>(undefined);
  todos = signal<UsuarioPublicoModel[]>([]);
  cargando = signal(true);
  error = signal('');
  nombre = computed(() => this.universidad()?.nombre ?? 'Universidad');

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

  list = computed(() => {
    const t = this.q().toLowerCase(), r = this.rango();
    return this.todos().filter(s => {
      const est = this.estudioEn(s);
      const sem = est?.semestre ?? 0;
      return (s.nombreCompleto + ' ' + (est?.carrera ?? '')).toLowerCase().includes(t) &&
        (r === 'todos' || (r === 'a' && sem <= 3) || (r === 'b' && sem >= 4 && sem <= 6) || (r === 'c' && sem >= 7));
    });
  });
}
