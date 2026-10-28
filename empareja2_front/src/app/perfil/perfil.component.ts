<<<<<<< HEAD
import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { EstudianteModel } from '../models/estudiante.model';
import { GeneroEnum } from '../models/genero.enum';
import { EstudianteService } from '../services/estudiante.service';
import { gradient, hueOf, initials } from '../utils/avatar.util';
=======
import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { UsuarioService } from '../services/usuario.service';
import { UsuarioPublicoModel } from '../models/usuario-publico.model';
import { fondoAvatar, gradient, hueOf, inicialesAvatar } from '../utils/avatar.util';
import { mensajeDeError } from '../utils/error.util';
>>>>>>> main

interface Datos { est?: EstudianteModel; otros: EstudianteModel[]; fallo?: boolean }

@Component({
  standalone: false,
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {
<<<<<<< HEAD
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
=======
  private usuarioService = inject(UsuarioService);
  private route = inject(ActivatedRoute);

  u = signal<UsuarioPublicoModel | undefined>(undefined);
  otros = signal<UsuarioPublicoModel[]>([]);
  error = signal('');
  aviso = signal('');
  enviando = signal(false);

  universidadPrincipal = computed(() => this.u()?.estudios[0]);
  volver = computed(() => {
    const id = this.universidadPrincipal()?.idUniversidad;
    return id ? ['/universidades', id] : ['/universidades'];
  });
  grad = gradient; hue = hueOf; fondo = fondoAvatar; ini = inicialesAvatar;
>>>>>>> main

  constructor() {
    this.route.paramMap.pipe(
      map(p => Number(p.get('id'))),
<<<<<<< HEAD
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
=======
      tap(() => { this.error.set(''); this.aviso.set(''); }),
      switchMap(id => this.usuarioService.obtenerPublico(id).pipe(
        map(u => ({ u, otros: [] as UsuarioPublicoModel[] })),
        /* PRUEBA B (compañeros de la universidad): borra la línea map(...) de arriba y descomenta este bloque
        switchMap(u => {
          const idUni = u.estudios[0]?.idUniversidad;
          const companeros$ = idUni === undefined ? of([] as UsuarioPublicoModel[]) :
            this.usuarioService.listarCatalogo(idUni).pipe(
              map(lista => lista.filter(x => x.id !== u.id).slice(0, 4)),
              catchError(() => of([] as UsuarioPublicoModel[])));
          return companeros$.pipe(map(otros => ({ u, otros })));
        }),
        fin PRUEBA B */
        catchError(err => {
          this.error.set(mensajeDeError(err, 'No encontramos este perfil.'));
          return of(null);
        }))),
      takeUntilDestroyed()
    ).subscribe(r => {
      if (r) { this.u.set(r.u); this.otros.set(r.otros); }
    });
  }

  /* PRUEBA A (me gusta): descomenta este método junto con el botón del html
  alternarMeGusta(): void {
    const st = this.u();
    if (!st || this.enviando()) return;
    this.enviando.set(true);
    this.aviso.set('');
    const accion = st.meGustaDelUsuarioActual ? this.usuarioService.quitarMeGusta(st.id) : this.usuarioService.darMeGusta(st.id);
    accion.subscribe({
      next: r => {
        this.u.set({ ...st, totalMeGusta: r.totalMeGusta, meGustaDelUsuarioActual: r.meGusta });
        this.enviando.set(false);
      },
      error: err => {
        this.aviso.set(mensajeDeError(err, 'No pudimos registrar tu me gusta.'));
        this.enviando.set(false);
      }
    });
  }
  fin PRUEBA A */
>>>>>>> main
}
