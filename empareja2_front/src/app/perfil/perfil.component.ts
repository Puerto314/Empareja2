import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { EstudianteModel } from '../models/estudiante.model';
import { GeneroEnum } from '../models/genero.enum';
import { ReaccionModel } from '../models/interaccion.model';
import { TipoReporteEnum } from '../models/tipo-reporte.enum';
import { AuthService } from '../services/auth.service';
import { EstudianteService } from '../services/estudiante.service';
import { InteraccionService } from '../services/interaccion.service';
import { gradient, hueOf, initials } from '../utils/avatar.util';
import { mensajeDeError } from '../utils/error.util';

interface Datos { est?: EstudianteModel; otros: EstudianteModel[]; fallo?: boolean }

@Component({
  standalone: false,
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {
  private estudianteService = inject(EstudianteService);
  private interaccion = inject(InteraccionService);
  private auth = inject(AuthService);
  private route = inject(ActivatedRoute);

  s = signal<EstudianteModel | undefined>(undefined);
  otros = signal<EstudianteModel[]>([]);
  cargando = signal(true);
  error = signal('');
  grad = gradient; hue = hueOf; ini = initials;

  // ---- Me gusta ----
  meGusta = signal(false);
  totalMeGusta = signal(0);
  quienes = signal<ReaccionModel[]>([]);
  errorMeGusta = signal('');
  trabajandoMeGusta = signal(false);

  // ---- Comentario privado ----
  comentario = '';
  enviandoComentario = signal(false);
  exitoComentario = signal('');
  errorComentario = signal('');

  // ---- Reporte ----
  readonly tiposReporte = [
    { v: TipoReporteEnum.SPAM, t: 'Spam' }, { v: TipoReporteEnum.ACOSO, t: 'Acoso' },
    { v: TipoReporteEnum.PERFIL_FALSO, t: 'Perfil falso' }, { v: TipoReporteEnum.CONTENIDO_INAPROPIADO, t: 'Contenido inapropiado' },
    { v: TipoReporteEnum.OTRO, t: 'Otro' }
  ];
  mostrandoReporte = signal(false);
  tipoReporte: TipoReporteEnum = TipoReporteEnum.SPAM;
  descripcionReporte = '';
  enviandoReporte = signal(false);
  exitoReporte = signal('');
  errorReporte = signal('');

  readonly generos: Record<GeneroEnum, string> = {
    [GeneroEnum.MASCULINO]: 'Masculino',
    [GeneroEnum.FEMENINO]: 'Femenino',
    [GeneroEnum.OTRO]: 'Otro',
    [GeneroEnum.NO_ESPECIFICA]: 'Prefiere no decirlo'
  };

  // Quien mira es el dueño del perfil, o un administrador (que modera desde /admin y no interactúa).
  esPropio = computed(() => this.s()?.id === this.auth.obtenerUsuario()?.id);
  puedeInteractuar = computed(() => !!this.s() && !this.esPropio() && !this.auth.esAdmin());

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
      tap(() => { this.cargando.set(true); this.error.set(''); this.reiniciarInteracciones(); }),
      switchMap(id => this.estudianteService.obtenerPorId(id).pipe(
        switchMap(est => est
          ? this.estudianteService.listarCompaneros(est).pipe(map((otros): Datos => ({ est, otros })))
          : of<Datos>({ otros: [] })),
        catchError(() => of<Datos>({ otros: [], fallo: true })))),
      takeUntilDestroyed()
    ).subscribe(datos => {
      this.s.set(datos.est);
      this.otros.set(datos.otros);
      this.meGusta.set(datos.est?.meGustaDelUsuarioActual ?? false);
      this.totalMeGusta.set(datos.est?.totalMeGusta ?? 0);
      this.error.set(datos.fallo ? 'No pudimos cargar este perfil. Revisa que el servidor esté encendido e intenta de nuevo.' : '');
      this.cargando.set(false);
      if (datos.est) { this.cargarQuienes(datos.est.id); }
    });
  }

  private reiniciarInteracciones(): void {
    this.quienes.set([]); this.errorMeGusta.set('');
    this.comentario = ''; this.exitoComentario.set(''); this.errorComentario.set('');
    this.mostrandoReporte.set(false); this.exitoReporte.set(''); this.errorReporte.set('');
    this.descripcionReporte = ''; this.tipoReporte = TipoReporteEnum.SPAM;
  }

  private cargarQuienes(id: number): void {
    this.interaccion.listarMeGusta(id).subscribe({ next: l => this.quienes.set(l), error: () => this.quienes.set([]) });
  }

  alternarMeGusta(): void {
    const est = this.s();
    if (!est || this.trabajandoMeGusta()) { return; }
    this.trabajandoMeGusta.set(true);
    this.errorMeGusta.set('');
    const peticion = this.meGusta() ? this.interaccion.quitarMeGusta(est.id) : this.interaccion.darMeGusta(est.id);
    peticion.subscribe({
      next: r => {
        this.meGusta.set(r.meGusta);
        this.totalMeGusta.set(r.totalMeGusta);
        this.trabajandoMeGusta.set(false);
        this.cargarQuienes(est.id);
      },
      error: (err: unknown) => {
        this.errorMeGusta.set(mensajeDeError(err, 'No pudimos registrar tu me gusta.'));
        this.trabajandoMeGusta.set(false);
      }
    });
  }

  enviarComentario(): void {
    const est = this.s();
    const texto = this.comentario.trim();
    this.exitoComentario.set('');
    if (!est) { return; }
    if (!texto) { this.errorComentario.set('Escribe el comentario antes de enviarlo.'); return; }
    if (texto.length > 1000) { this.errorComentario.set('El comentario no puede superar 1000 caracteres.'); return; }
    this.errorComentario.set('');
    this.enviandoComentario.set(true);
    this.interaccion.enviarComentario(est.id, texto).subscribe({
      next: () => {
        this.comentario = '';
        this.enviandoComentario.set(false);
        this.exitoComentario.set(`Comentario enviado. Solo ${est.nombre} puede leerlo.`);
      },
      error: (err: unknown) => {
        this.errorComentario.set(mensajeDeError(err, 'No pudimos enviar el comentario.'));
        this.enviandoComentario.set(false);
      }
    });
  }

  alternarReporte(): void { this.mostrandoReporte.update(v => !v); this.exitoReporte.set(''); this.errorReporte.set(''); }

  enviarReporte(): void {
    const est = this.s();
    if (!est) { return; }
    const descripcion = this.descripcionReporte.trim();
    if (this.tipoReporte === TipoReporteEnum.OTRO && !descripcion) {
      this.errorReporte.set('Describe el motivo cuando eliges "Otro".');
      return;
    }
    this.errorReporte.set('');
    this.enviandoReporte.set(true);
    this.interaccion.reportar(est.id, { tipoReporte: this.tipoReporte, ...(descripcion ? { descripcion } : {}) }).subscribe({
      next: () => {
        this.enviandoReporte.set(false);
        this.mostrandoReporte.set(false);
        this.descripcionReporte = '';
        this.exitoReporte.set('Reporte enviado. Un administrador lo revisará.');
      },
      error: (err: unknown) => {
        this.errorReporte.set(mensajeDeError(err, 'No pudimos enviar el reporte.'));
        this.enviandoReporte.set(false);
      }
    });
  }
}
