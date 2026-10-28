import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { EstudianteService } from '../services/estudiante.service';
import { gradient, hueOf, initials } from '../utils/avatar.util';

@Component({
  standalone: false,
  selector: 'app-universidades',
  templateUrl: './universidades.component.html',
  styleUrls: ['./universidades.component.css']
})
export class UniversidadesComponent {
  private estudianteService = inject(EstudianteService);

  unis = toSignal(this.estudianteService.listarUniversidades(), { initialValue: [] });
  total = computed(() => this.unis().reduce((suma, u) => suma + u.cantidad, 0));
  grad = gradient; hue = hueOf; ini = initials;
}
