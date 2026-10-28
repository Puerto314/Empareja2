import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { EstudianteModel } from '../models/estudiante.model';
import { GeneroEnum } from '../models/genero.enum';
import { EstudianteService } from '../services/estudiante.service';
import { gradient, hueOf, initials } from '../utils/avatar.util';

interface Datos { est?: EstudianteModel; otros: EstudianteModel[]; fallo?: boolean }

@Component({
  standalone: false,
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {
  private estudianteService = inject(EstudianteService);
  private route = inject(ActivatedRoute);

  s = signal<EstudianteModel | undefined>(undefined);
  otros = signal<EstudianteModel[]>([]);
  cargando = signal(true);
  error = signal('');
  grad = gradient; hue = hueOf; ini = initials;

  readonly generos: Record<GeneroEnum, string> = {
    [GeneroEnum.MASCULINO]: 'Masculino',
    [GeneroEnum.FEMENINO]: 'Femenino',
    [GeneroEnum.OTRO]: 'Otro',
    [GeneroEnum.NO_ESPECIFICA]: 'Prefiere no decirlo'
  };

  // Texto del género; vacío si no lo especificó.
  generoDe(est: EstudianteModel): string {
    return est.genero && est.genero !== GeneroEnum.NO_ESPECIFICA ? this.generos[est.genero] : '';
  }

  carreraDe(est: EstudianteModel): string {
    return est.estudios.length ? est.estudios[0].carrera : '';
  }

  constructor() {
    this.route.paramMap.pipe(
      map(p => Number(p.get('id'))),
      tap(() => { this.cargando.set(true); this.error.set(''); }),
      switchMap(id => this.estudianteService.obtenerPorId(id).pipe(
        switchMap(est => est
          ? this.estudianteService.listarCompaneros(est).pipe(map((otros): Datos => ({ est, otros })))
          : of<Datos>({ otros: [] })),
        catchError(() => of<Datos>({ otros: [], fallo: true })))),
      takeUntilDestroyed()
    ).subscribe(datos => {
      this.s.set(datos.est);
      this.otros.set(datos.otros);
      this.error.set(datos.fallo ? 'No pudimos cargar este perfil. Revisa que el servidor esté encendido e intenta de nuevo.' : '');
      this.cargando.set(false);
    });
  }
}
