import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { CarreraModel, OfertaModel } from '../models/catalogo.model';
import { CiudadModel, UniversidadModel } from '../models/universidad.model';
import { AdminService } from '../services/admin.service';
import { CatalogoService } from '../services/catalogo.service';
import { UniversidadService } from '../services/universidad.service';
import { mensajeDeError } from '../utils/error.util';

type Seccion = 'ciudades' | 'universidades' | 'carreras' | 'oferta';

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
    { id: 'oferta', t: 'Oferta académica' }
  ];
  seccion = signal<Seccion>('ciudades');

  ciudades = signal<CiudadModel[]>([]);
  universidades = signal<UniversidadModel[]>([]);
  carreras = signal<CarreraModel[]>([]);
  oferta = signal<OfertaModel[]>([]);

  cargando = signal(true);
  error = signal('');
  exito = signal('');
  confirmando = signal('');

  fCiudad: { id: number | null; nombre: string; departamento: string } = { id: null, nombre: '', departamento: '' };
  fUni: { id: number | null; nombre: string; sigla: string; calle: string; barrio: string; idCiudad: number | null } =
    { id: null, nombre: '', sigla: '', calle: '', barrio: '', idCiudad: null };
  fCarrera: { id: number | null; nombre: string; facultad: string } = { id: null, nombre: '', facultad: '' };
  fOferta: { editando: boolean; idCarrera: number | null; idUniversidad: number | null; facultad: string } =
    { editando: false, idCarrera: null, idUniversidad: null, facultad: '' };
  busquedaUni = '';

  constructor() { this.cargar(); }

  cambiarSeccion(s: Seccion): void { this.seccion.set(s); this.error.set(''); this.exito.set(''); this.confirmando.set(''); }

  cargar(): void {
    this.cargando.set(true);
    forkJoin({
      ciudades: this.catalogo.ciudades(), universidades: this.universidadService.listar(this.busquedaUni.trim() || undefined),
      carreras: this.catalogo.carreras(), oferta: this.catalogo.oferta()
    }).subscribe({
      next: r => {
        this.ciudades.set(r.ciudades); this.universidades.set(r.universidades); this.carreras.set(r.carreras);
        this.oferta.set(r.oferta); this.cargando.set(false);
      },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar los catálogos.')); this.cargando.set(false); }
    });
  }

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

  editarUniversidad(u: UniversidadModel): void {
    this.universidadService.obtener(u.idUniversidad).subscribe({
      next: x => {
        if (!x) { this.error.set('La universidad ya no existe.'); return; }
        this.fUni = { id: x.idUniversidad, nombre: x.nombre, sigla: x.sigla, calle: x.calle ?? '', barrio: x.barrio ?? '', idCiudad: x.ciudad?.idCiudad ?? null };
      },
      error: e => this.fallo(e, 'No pudimos abrir la universidad.')
    });
  }
  guardarUniversidad(): void {
    const f = this.fUni;
    if (!f.nombre.trim() || !f.sigla.trim() || !f.calle.trim() || !f.barrio.trim() || f.idCiudad === null) { this.error.set('Escribe nombre, sigla, calle y barrio, y elige la ciudad.'); return; }
    const datos = {
      nombre: f.nombre.trim(), sigla: f.sigla.trim(), idCiudad: f.idCiudad,
      calle: f.calle.trim(), barrio: f.barrio.trim()
    };
    (f.id === null ? this.admin.crearUniversidad(datos) : this.admin.actualizarUniversidad(f.id, datos)).subscribe({
      next: () => this.ok(f.id === null ? 'Universidad creada.' : 'Universidad actualizada.', () => (this.fUni = { id: null, nombre: '', sigla: '', calle: '', barrio: '', idCiudad: null })),
      error: e => this.fallo(e, 'No pudimos guardar la universidad.')
    });
  }
  eliminarUniversidad(u: UniversidadModel): void {
    this.admin.eliminarUniversidad(u.idUniversidad).subscribe({ next: () => this.ok('Universidad eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la universidad.') });
  }

  editarCarrera(c: CarreraModel): void {
    this.catalogo.carrera(c.idCarrera).subscribe({
      next: x => (this.fCarrera = { id: x.idCarrera, nombre: x.nombre, facultad: x.facultad }),
      error: e => this.fallo(e, 'No pudimos abrir la carrera.')
    });
  }
  guardarCarrera(): void {
    const f = this.fCarrera;
    if (!f.nombre.trim() || !f.facultad.trim()) { this.error.set('Escribe el nombre y la facultad de la carrera.'); return; }
    const datos = { nombre: f.nombre.trim(), facultad: f.facultad.trim() };
    (f.id === null ? this.admin.crearCarrera(datos) : this.admin.actualizarCarrera(f.id, datos)).subscribe({
      next: () => this.ok(f.id === null ? 'Carrera creada.' : 'Carrera actualizada.', () => (this.fCarrera = { id: null, nombre: '', facultad: '' })),
      error: e => this.fallo(e, 'No pudimos guardar la carrera.')
    });
  }
  eliminarCarrera(c: CarreraModel): void {
    this.admin.eliminarCarrera(c.idCarrera).subscribe({ next: () => this.ok('Carrera eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la carrera.') });
  }

  editarOferta(o: OfertaModel): void {
    this.fOferta = { editando: true, idCarrera: o.idCarrera, idUniversidad: o.idUniversidad, facultad: o.facultad };
  }
  cancelarOferta(): void { this.fOferta = { editando: false, idCarrera: null, idUniversidad: null, facultad: '' }; }
  guardarOferta(): void {
    const f = this.fOferta;
    if (f.idCarrera === null || f.idUniversidad === null || (f.editando && !f.facultad.trim())) { this.error.set(f.editando ? 'Escribe la facultad.' : 'Elige carrera y universidad.'); return; }
    const facultad = f.facultad.trim();
    (f.editando
      ? this.admin.actualizarOferta(f.idCarrera, f.idUniversidad, facultad)
      : this.admin.crearOferta({ idCarrera: f.idCarrera, idUniversidad: f.idUniversidad })).subscribe({
      next: () => this.ok(f.editando ? 'Oferta actualizada.' : 'Oferta creada.', () => this.cancelarOferta()),
      error: e => this.fallo(e, 'No pudimos guardar la oferta.')
    });
  }
  eliminarOferta(o: OfertaModel): void {
    this.admin.eliminarOferta(o.idCarrera, o.idUniversidad).subscribe({ next: () => this.ok('Oferta eliminada.'), error: e => this.fallo(e, 'No pudimos eliminar la oferta.') });
  }
}
