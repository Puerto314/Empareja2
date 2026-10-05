import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { STUDENTS, gradient, hueOf, initials, universitiesOf } from '../../mock-data';

@Component({
  selector: 'app-universidades',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './universidades.component.html',
  styleUrls: ['./universidades.component.css']
})
export class UniversidadesComponent {
  total = STUDENTS.length;
  grad = gradient; ini = initials;
  tiles = universitiesOf(STUDENTS).map(name => {
    const students = STUDENTS.filter(s => s.university === name);
    return { name, hue: hueOf(name), count: students.length, preview: students.slice(0, 4),
      sigla: name.split(' ').filter(w => /^[A-ZÁÉÍÓÚ]/.test(w)).map(w => w[0]).join('') };
  });
  hue = hueOf;
}
