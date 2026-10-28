import { Component, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { EstudianteService } from '../services/estudiante.service';
import { gradient, hueOf, initials, siglaDe } from '../utils/avatar.util';

@Component({
  standalone: false,
  selector: 'app-estudiantes',
  templateUrl: './estudiantes.component.html',
  styleUrls: ['./estudiantes.component.css']
})
export class EstudiantesComponent {
  private route = inject(ActivatedRoute);
  private estudianteService = inject(EstudianteService);

  private nombre$ = this.route.paramMap.pipe(map(p => p.get('id') ?? ''));
  uni = toSignal(this.nombre$, { initialValue: '' });
  todos = toSignal(this.nombre$.pipe(switchMap(n => this.estudianteService.listarPorUniversidad(n))), { initialValue: [] });
  sigla = computed(() => siglaDe(this.uni()));

  q = signal('');
  rango = signal<'todos' | 'a' | 'b' | 'c'>('todos');
  filtros = [
    { id: 'todos', label: 'Todos' }, { id: 'a', label: 'Semestres 1-3' },
    { id: 'b', label: 'Semestres 4-6' }, { id: 'c', label: 'Semestre 7 o más' }
  ] as const;
  grad = gradient; hue = hueOf; ini = initials;

  list = computed(() => {
    const t = this.q().toLowerCase(), r = this.rango();
    return this.todos().filter(s =>
      (s.fullName + ' ' + s.career).toLowerCase().includes(t) &&
      (r === 'todos' || (r === 'a' && s.semester <= 3) || (r === 'b' && s.semester >= 4 && s.semester <= 6) || (r === 'c' && s.semester >= 7)));
  });
}
