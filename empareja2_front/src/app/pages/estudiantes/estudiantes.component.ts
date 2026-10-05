import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { STUDENTS, gradient, hueOf, initials } from '../../mock-data';

@Component({
  selector: 'app-estudiantes',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './estudiantes.component.html',
  styleUrls: ['./estudiantes.component.css']
})
export class EstudiantesComponent {
  uni = inject(ActivatedRoute).snapshot.paramMap.get('id') ?? '';
  sigla = this.uni.split(' ').filter(w => /^[A-ZÁÉÍÓÚ]/.test(w)).map(w => w[0]).join('');
  all = STUDENTS.filter(s => s.university === this.uni);
  q = signal('');
  rango = signal<'todos' | 'a' | 'b' | 'c'>('todos');
  filtros = [
    { id: 'todos', label: 'Todos' }, { id: 'a', label: 'Semestres 1-3' },
    { id: 'b', label: 'Semestres 4-6' }, { id: 'c', label: 'Semestre 7 o más' }
  ] as const;
  grad = gradient; hue = hueOf; ini = initials;

  list = computed(() => {
    const t = this.q().toLowerCase(), r = this.rango();
    return this.all.filter(s =>
      (s.fullName + ' ' + s.career).toLowerCase().includes(t) &&
      (r === 'todos' || (r === 'a' && s.semester <= 3) || (r === 'b' && s.semester >= 4 && s.semester <= 6) || (r === 'c' && s.semester >= 7)));
  });
}
