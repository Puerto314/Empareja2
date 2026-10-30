import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { CarreraModel, OfertaModel, RedSocialModel } from '../models/catalogo.model';
import { CiudadModel, UniversidadModel } from '../models/universidad.model';
import { AdminService } from '../services/admin.service';
import { CatalogoService } from '../services/catalogo.service';
import { UniversidadService } from '../services/universidad.service';
import { mensajeDeError } from '../utils/error.util';

type Seccion = 'ciudades' | 'universidades' | 'carreras' | 'redes' | 'oferta';

// Datos de referencia: ciudades, universidades, carreras, redes sociales y oferta académica (crear, editar, eliminar).
@Component({
  standalone: false,
  selector: 'app-admin-catalogos',
  templateUrl: './admin-catalogos.component.html'
})
export class AdminCatalogosComponent {
  private admin = inject(AdminService);
  private catalogo = inject(CatalogoService);
  private universidadService = inject(UniversidadService);

  readonly secciones: { id: Seccion; t: string }[] = [
    { id: 'ciudades', t: 'Ciudades' }, { id: 'universidades', t: 'Universidades' }, { id: 'carreras', t: 'Carreras' },
    { id: 'redes', t: 'Redes sociales' }, { id: 'oferta', t: 'Oferta académica' }
  ];
  seccion = signal<Seccion>('ciudades');

  ciudades = signal<CiudadModel[]>([]);
  universidades = signal<UniversidadModel[]>([]);
  carreras = signal<CarreraModel[]>([]);
  redes = signal<RedSocialModel[]>([]);
  oferta = signal<OfertaModel[]>([]);

  cargando = signal(true);
  error = signal('');
  exito = signal('');
  confirmando = signal('');

  // Formularios (id = null => crear; con id => editar)
  fCiudad: { id: number | null; nombre: string; departamento: string } = { id: null, nombre: '', departamento: '' };
  fUni: { id: number | null; nombre: string; sigla: string; calle: string; barrio: string; numeroDireccion: string; idCiudad: number | null } =
    { id: null, nombre: '', sigla: '', calle: '', barrio: '', numeroDireccion: '', idCiudad: null };
  fCarrera: { id: number | null; nombre: string } = { id: null, nombre: '' };
  fRed: { id: number | null; nombreRedSocial: string; url: string } = { id: null, nombreRedSocial: '', url: '' };
  fOferta: { editando: boolean; idCarrera: number | null; idUniversidad: number | null; facultad: string } =
    { editando: false, idCarrera: null, idUniversidad: null, facultad: '' };
  busquedaUni = '';

  constructor() { this.cargar(); }

  cambiarSeccion(s: Seccion): void { this.seccion.set(s); this.error.set(''); this.exito.set(''); this.confirmando.set(''); }

  cargar(): void {
    this.cargando.set(true);
    forkJoin({
      ciudades: this.catalogo.ciudades(), universidades: this.universidadService.listar(this.busquedaUni.trim() || undefined),
      carreras: this.catalogo.carreras(), redes: this.catalogo.redesSociales(), oferta: this.catalogo.oferta()
    }).subscribe({
      next: r => {
        this.ciudades.set(r.ciudades); this.universidades.set(r.universidades); this.carreras.set(r.carreras);
        this.redes.set(r.redes); this.oferta.set(r.oferta); this.cargando.set(false);
      },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar los catálogos.')); this.cargando.set(false); }
    });
  }

  // Resultado común de guardar/eliminar.
  private ok(mensaje: string, limpiar?: () => void): void {
    this.error.set(''); this.confirmando.set(''); this.exito.set(mensaje);
    limpiar?.();
    this.cargar();
  }
  private fallo(err: unknown, porDefecto: string): void {
    this.exito.set(''); this.confirmando.set(''); this.error.set(mensajeDeError(err, porDefecto));
  }
  pedir(clave: string): void { this.confirmando.set(clave); }
  cancelarPedido(): void { this.confirmando.set(''); }

  // ---- Ciudades ----
  editarCiudad(c: CiudadModel): void {
    this.catalogo.ciudad(c.idCiudad).subscribe({
      next: x => (this.fCiudad = { id: x.idCiudad, nombre: x.nombre, departamento: x.departamento }),
      error: e => this.fallo(e, 'No pudimos abrir la ciudad.')
    });
  }
  guardarCiudad(): void {
    const f = this.fCiudad;
    if (!f.nombre.trim() || !f.departamento.trim()) { this.error.set('Escribe el nombre y el departamento.'); return; }
    const datos = { nombre: f.nombre.trim(), departamento: f.departamento.trim() };
    (f.id === null ? this.admin.crearCiudad(datos) : this.admin.actualizarCiudad(f.id, datos)).subscribe({
      next: () => this.ok(f.id === null ? 'Ciudad creada.' : 'Ciudad actualizada.', () => (this.fCiudad = { id: null, nombre: '', departamento: '' })),
      error: e => this.fallo(e, 'No pudimos guardar la ciudad.')
    });
  }
  eliminarCiudad(c: CiudadModel): void {
    this.admin.eliminarCiudad(c.idCiudad).subscribe({ next: () => this.ok('Ciudad eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la ciudad.') });
  }

  // ---- Universidades ----
  editarUniversidad(u: UniversidadModel): void {
    this.universidadService.obtener(u.idUniversidad).subscribe({
      next: x => {
        if (!x) { this.error.set('La universidad ya no existe.'); return; }
        this.fUni = { id: x.idUniversidad, nombre: x.nombre, sigla: x.sigla, calle: x.calle ?? '', barrio: x.barrio ?? '', numeroDireccion: x.numeroDireccion ?? '', idCiudad: x.ciudad?.idCiudad ?? null };
      },
      error: e => this.fallo(e, 'No pudimos abrir la universidad.')
    });
  }
  guardarUniversidad(): void {
    const f = this.fUni;
    if (!f.nombre.trim() || !f.sigla.trim() || f.idCiudad === null) { this.error.set('Escribe nombre y sigla, y elige la ciudad.'); return; }
    const datos = {
      nombre: f.nombre.trim(), sigla: f.sigla.trim(), idCiudad: f.idCiudad,
      ...(f.calle.trim() ? { calle: f.calle.trim() } : {}), ...(f.barrio.trim() ? { barrio: f.barrio.trim() } : {}),
      ...(f.numeroDireccion.trim() ? { numeroDireccion: f.numeroDireccion.trim() } : {})
    };
    (f.id === null ? this.admin.crearUniversidad(datos) : this.admin.actualizarUniversidad(f.id, datos)).subscribe({
      next: () => this.ok(f.id === null ? 'Universidad creada.' : 'Universidad actualizada.', () => (this.fUni = { id: null, nombre: '', sigla: '', calle: '', barrio: '', numeroDireccion: '', idCiudad: null })),
      error: e => this.fallo(e, 'No pudimos guardar la universidad.')
    });
  }
  eliminarUniversidad(u: UniversidadModel): void {
    this.admin.eliminarUniversidad(u.idUniversidad).subscribe({ next: () => this.ok('Universidad eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la universidad.') });
  }

  // ---- Carreras ----
  editarCarrera(c: CarreraModel): void {
    this.catalogo.carrera(c.idCarrera).subscribe({
      next: x => (this.fCarrera = { id: x.idCarrera, nombre: x.nombre }),
      error: e => this.fallo(e, 'No pudimos abrir la carrera.')
    });
  }
  guardarCarrera(): void {
    const f = this.fCarrera;
    if (!f.nombre.trim()) { this.error.set('Escribe el nombre de la carrera.'); return; }
    const datos = { nombre: f.nombre.trim() };
    (f.id === null ? this.admin.crearCarrera(datos) : this.admin.actualizarCarrera(f.id, datos)).subscribe({
      next: () => this.ok(f.id === null ? 'Carrera creada.' : 'Carrera actualizada.', () => (this.fCarrera = { id: null, nombre: '' })),
      error: e => this.fallo(e, 'No pudimos guardar la carrera.')
    });
  }
  eliminarCarrera(c: CarreraModel): void {
    this.admin.eliminarCarrera(c.idCarrera).subscribe({ next: () => this.ok('Carrera eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la carrera.') });
  }

  // ---- Redes sociales ----
  editarRed(r: RedSocialModel): void {
    this.catalogo.redSocial(r.idRedSocial).subscribe({
      next: x => (this.fRed = { id: x.idRedSocial, nombreRedSocial: x.nombreRedSocial, url: x.url }),
      error: e => this.fallo(e, 'No pudimos abrir la red social.')
    });
  }
  guardarRed(): void {
    const f = this.fRed;
    if (!f.nombreRedSocial.trim() || !/^https?:\/\/\S+$/.test(f.url.trim())) { this.error.set('Escribe el nombre y una url que empiece por http:// o https://.'); return; }
    const datos = { nombreRedSocial: f.nombreRedSocial.trim(), url: f.url.trim() };
    (f.id === null ? this.admin.crearRedSocial(datos) : this.admin.actualizarRedSocial(f.id, datos)).subscribe({
      next: () => this.ok(f.id === null ? 'Red social creada.' : 'Red social actualizada.', () => (this.fRed = { id: null, nombreRedSocial: '', url: '' })),
      error: e => this.fallo(e, 'No pudimos guardar la red social.')
    });
  }
  eliminarRed(r: RedSocialModel): void {
    this.admin.eliminarRedSocial(r.idRedSocial).subscribe({ next: () => this.ok('Red social eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la red social.') });
  }

  // ---- Oferta académica ----
  editarOferta(o: OfertaModel): void {
    this.fOferta = { editando: true, idCarrera: o.idCarrera, idUniversidad: o.idUniversidad, facultad: o.facultad };
  }
  cancelarOferta(): void { this.fOferta = { editando: false, idCarrera: null, idUniversidad: null, facultad: '' }; }
  guardarOferta(): void {
    const f = this.fOferta;
    if (f.idCarrera === null || f.idUniversidad === null || !f.facultad.trim()) { this.error.set('Elige carrera y universidad, y escribe la facultad.'); return; }
    const facultad = f.facultad.trim();
    (f.editando
      ? this.admin.actualizarOferta(f.idCarrera, f.idUniversidad, facultad)
      : this.admin.crearOferta({ idCarrera: f.idCarrera, idUniversidad: f.idUniversidad, facultad })).subscribe({
      next: () => this.ok(f.editando ? 'Oferta actualizada.' : 'Oferta creada.', () => this.cancelarOferta()),
      error: e => this.fallo(e, 'No pudimos guardar la oferta.')
    });
  }
  eliminarOferta(o: OfertaModel): void {
    this.admin.eliminarOferta(o.idCarrera, o.idUniversidad).subscribe({ next: () => this.ok('Oferta eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la oferta.') });
  }
}
