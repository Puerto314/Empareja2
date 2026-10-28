import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { UniversidadResumenModel } from '../models/universidad.model';
import { UniversidadService } from '../services/universidad.service';
import { gradient, hueOf, initials, siglaCorta } from '../utils/avatar.util';

@Component({
  standalone: false,
  selector: 'app-universidades',
  templateUrl: './universidades.component.html',
  styleUrls: ['./universidades.component.css']
})
export class UniversidadesComponent {
  private universidadService = inject(UniversidadService);

  unis = signal<UniversidadResumenModel[]>([]);
  cargando = signal(true);
  error = signal('');
  total = signal(0);
  grad = gradient; hue = hueOf; ini = initials; sigla = siglaCorta;

  constructor() {
    this.universidadService.listarResumen().pipe(takeUntilDestroyed()).subscribe({
      next: r => { this.unis.set(r.universidades); this.total.set(r.totalEstudiantes); this.cargando.set(false); },
      error: () => {
        this.error.set('No pudimos cargar las universidades. Revisa que el servidor esté encendido e intenta de nuevo.');
        this.cargando.set(false);
      }
    });
  }
}
