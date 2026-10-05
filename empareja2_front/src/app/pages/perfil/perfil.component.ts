import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { STUDENTS, gradient, hueOf, initials } from '../../mock-data';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {
  private id = toSignal(inject(ActivatedRoute).paramMap.pipe(map(p => Number(p.get('id')))));
  s = computed(() => STUDENTS.find(x => x.id === this.id()));
  otros = computed(() => STUDENTS.filter(x => x.university === this.s()?.university && x.id !== this.s()?.id).slice(0, 4));
  vacias = [0, 1, 2];
  grad = gradient; hue = hueOf; ini = initials;
}
