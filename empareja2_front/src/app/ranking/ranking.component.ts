import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { EstudianteModel } from '../models/estudiante.model';
import { UniversidadModel } from '../models/universidad.model';
import { EstudianteService } from '../services/estudiante.service';
import { UniversidadService } from '../services/universidad.service';
import { gradient, hueOf, initials } from '../utils/avatar.util';

interface Datos { lista: EstudianteModel[]; fallo?: boolean }

// Top 20 de usuarios con más "me gusta": general o filtrado por universidad (?universidad=ID).
@Component({
  standalone: false,
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private estudianteService = inject(EstudianteService);
  private universidadService = inject(UniversidadService);

  readonly limite = 20;
  universidades = signal<UniversidadModel[]>([]);
  idUniversidad = signal<number | null>(null);
  ranking = signal<EstudianteModel[]>([]);
  cargando = signal(true);
  error = signal('');
  grad = gradient; hue = hueOf; ini = initials;

  constructor() {
    this.universidadService.listar().pipe(takeUntilDestroyed()).subscribe({
      next: l => this.universidades.set(l),
      error: () => this.universidades.set([])
    });
    this.route.queryParamMap.pipe(
      map(p => (p.get('universidad') ? Number(p.get('universidad')) : null)),
      tap(id => { this.idUniversidad.set(id); this.cargando.set(true); this.error.set(''); }),
      switchMap(id => this.estudianteService.ranking(id ?? undefined, this.limite).pipe(
        map((lista): Datos => ({ lista })),
        catchError(() => of<Datos>({ lista: [], fallo: true })))),
      takeUntilDestroyed()
    ).subscribe(d => {
      this.ranking.set(d.lista);
      this.error.set(d.fallo ? 'No pudimos cargar el ranking. Revisa que el servidor esté encendido e intenta de nuevo.' : '');
      this.cargando.set(false);
    });
  }

  nombreUniversidad(): string {
    const id = this.idUniversidad();
    return this.universidades().find(u => u.idUniversidad === id)?.nombre ?? '';
  }

  elegir(valor: string): void {
    this.router.navigate(['/ranking'], { queryParams: valor ? { universidad: valor } : {} });
  }
}
