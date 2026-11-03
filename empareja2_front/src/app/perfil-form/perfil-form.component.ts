import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { OfertaModel, RedSocialModel } from '../models/catalogo.model';
import { GeneroEnum } from '../models/genero.enum';
import { ActualizarPerfilRequestModel, AdminUsuarioRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { RegistroRequestModel } from '../models/registro-request.model';
import { RolEnum } from '../models/rol.enum';
import { UniversidadModel } from '../models/universidad.model';
import { CatalogoService } from '../services/catalogo.service';
import { UniversidadService } from '../services/universidad.service';

export type ModoPerfilForm = 'registro' | 'edicion' | 'admin';
export type PerfilFormValor = RegistroRequestModel | ActualizarPerfilRequestModel | AdminUsuarioRequestModel;

interface FilaEstudio { idUniversidad: number | null; idCarrera: number | null; semestre: number | null; }
interface FilaRed { idRedSocial: number | null; nombreUsuario: string; }

const EDAD_MINIMA = 18; // igual a Usuario.EDAD_MINIMA del backend

// Formulario de perfil que comparten el registro, "Mi perfil" (edición) y la administración de usuarios.
@Component({
  standalone: false,
  selector: 'app-perfil-form',
  templateUrl: './perfil-form.component.html',
  styleUrls: ['./perfil-form.component.css']
})
export class PerfilFormComponent implements OnInit, OnChanges {
  private catalogo = inject(CatalogoService);
  private universidadService = inject(UniversidadService);

  @Input() modo: ModoPerfilForm = 'registro';
  @Input() inicial: PerfilModel | null = null;
  @Input() cargando = false;
  @Input() error = '';
  @Input() textoBoton = 'Guardar';
  @Output() enviar = new EventEmitter<PerfilFormValor>();
  @Output() cancelar = new EventEmitter<void>();

  universidades = signal<UniversidadModel[]>([]);
  oferta = signal<OfertaModel[]>([]);
  redesDisponibles = signal<RedSocialModel[]>([]);
  errorCatalogo = signal('');
  errorLocal = signal('');

  readonly generos = [
    { v: GeneroEnum.FEMENINO, t: 'Femenino' }, { v: GeneroEnum.MASCULINO, t: 'Masculino' },
    { v: GeneroEnum.OTRO, t: 'Otro' }, { v: GeneroEnum.NO_ESPECIFICA, t: 'Prefiero no decirlo' }
  ];
  readonly rolesDisponibles = [RolEnum.ESTUDIANTE, RolEnum.ADMIN];

  correo = '';
  contrasena = '';
  rol: RolEnum = RolEnum.ESTUDIANTE;
  genero: GeneroEnum | '' = GeneroEnum.NO_ESPECIFICA;
  nombre = '';
  apellido = '';
  fechaNacimiento = '';
  fotoPerfil = '';
  interesesTexto = '';
  estudios: FilaEstudio[] = [];
  redes: FilaRed[] = [];

  get esEstudiante(): boolean { return this.modo !== 'admin' || this.rol === RolEnum.ESTUDIANTE; }
  get pideCuenta(): boolean { return this.modo !== 'edicion'; }
  get editandoExistente(): boolean { return this.inicial !== null; }

  ngOnInit(): void {
    forkJoin({
      universidades: this.universidadService.listar(),
      oferta: this.catalogo.oferta(),
      redes: this.catalogo.redesSociales()
    }).subscribe({
      next: r => {
        this.universidades.set(r.universidades);
        this.oferta.set(r.oferta);
        this.redesDisponibles.set(r.redes);
      },
      error: () => this.errorCatalogo.set('No pudimos cargar universidades, carreras y redes sociales. Recarga la página.')
    });
    if (this.modo === 'registro' && this.estudios.length === 0) { this.agregarEstudio(); }
  }

  ngOnChanges(cambios: SimpleChanges): void {
    if (cambios['inicial']) { this.cargarInicial(); }
  }

  private cargarInicial(): void {
    const p = this.inicial;
    if (!p) { return; }
    this.correo = p.correo;
    this.contrasena = '';
    this.rol = p.rol;
    this.genero = p.genero ?? GeneroEnum.NO_ESPECIFICA;
    this.nombre = p.nombre;
    this.apellido = p.apellido;
    this.fechaNacimiento = p.fechaNacimiento ?? '';
    this.fotoPerfil = p.fotoPerfil ?? '';
    this.interesesTexto = p.intereses.join(', ');
    this.estudios = p.estudios.map(e => ({ idUniversidad: e.idUniversidad, idCarrera: e.idCarrera, semestre: e.semestre }));
    this.redes = p.redes.map(r => ({ idRedSocial: r.idRedSocial, nombreUsuario: r.nombreUsuario }));
  }

  carrerasDe(idUniversidad: number | null): OfertaModel[] {
    return idUniversidad === null ? [] : this.oferta().filter(o => o.idUniversidad === idUniversidad);
  }

  cambiarUniversidad(fila: FilaEstudio): void { fila.idCarrera = null; }
  agregarEstudio(): void { this.estudios.push({ idUniversidad: null, idCarrera: null, semestre: null }); }
  agregarRed(): void { this.redes.push({ idRedSocial: null, nombreUsuario: '' }); }
  quitarRed(i: number): void { this.redes.splice(i, 1); }

  private intereses(): string[] {
    // Sin repetidos ignorando mayúsculas ("Cine" y "cine" chocarían en la base de datos); se conserva la primera.
    const vistos = new Set<string>();
    return this.interesesTexto.split(',').map(t => t.trim()).filter(t => {
      const clave = t.toLowerCase();
      if (!t || vistos.has(clave)) { return false; }
      vistos.add(clave);
      return true;
    });
  }

  private edadDe(fecha: string): number {
    const f = new Date(fecha + 'T00:00:00');
    const hoy = new Date();
    let edad = hoy.getFullYear() - f.getFullYear();
    if (hoy.getMonth() < f.getMonth() || (hoy.getMonth() === f.getMonth() && hoy.getDate() < f.getDate())) { edad--; }
    return edad;
  }

  // Validaciones rápidas (las mismas reglas del backend); devuelve el primer problema o ''.
  validar(): string {
    const correo = this.correo.trim();
    if (this.pideCuenta) {
      if (!correo) { return 'Escribe el correo.'; }
      if (this.esEstudiante && !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(correo)) { return 'El correo no tiene un formato válido.'; }
      const sinClave = this.modo === 'admin' && this.editandoExistente && !this.contrasena;
      if (!sinClave && (this.contrasena.length < 8 || this.contrasena.length > 72)) {
        return 'La contraseña debe tener entre 8 y 72 caracteres.';
      }
    }
    if (!this.nombre.trim()) { return 'Escribe tu nombre.'; }
    if (!this.apellido.trim()) { return 'Escribe tu apellido.'; }
    if (this.esEstudiante && !this.fechaNacimiento) { return 'Indica tu fecha de nacimiento.'; }
    if (this.fechaNacimiento) {
      if (this.edadDe(this.fechaNacimiento) < 0) { return 'La fecha de nacimiento debe ser en el pasado.'; }
      if (this.edadDe(this.fechaNacimiento) < EDAD_MINIMA) { return `Debes tener al menos ${EDAD_MINIMA} años.`; }
    }
    if (this.fotoPerfil.trim() && !/^https?:\/\/\S+$/.test(this.fotoPerfil.trim())) {
      return 'La foto de perfil debe ser un enlace que empiece por http:// o https://.';
    }
    if (this.esEstudiante) {
      if (this.estudios.length !== 1) { return 'Indica tu universidad, carrera y semestre.'; }
      const e = this.estudios[0];
      if (e.idUniversidad === null || e.idCarrera === null || e.semestre === null) {
        return 'Completa universidad, carrera y semestre.';
      }
      if (e.semestre < 1 || e.semestre > 20) { return 'El semestre debe estar entre 1 y 20.'; }
    }
    const idsRed = new Set<number>();
    for (const r of this.redes) {
      if (r.idRedSocial === null || !r.nombreUsuario.trim()) {
        return 'Cada red social necesita la red y tu nombre de usuario en ella.';
      }
      if (r.nombreUsuario.trim().length > 50) { return 'El nombre de usuario de una red social admite máximo 50 caracteres.'; }
      if (idsRed.has(r.idRedSocial)) { return 'No repitas la misma red social.'; }
      idsRed.add(r.idRedSocial);
    }
    return '';
  }

  guardar(): void {
    const problema = this.validar();
    this.errorLocal.set(problema);
    if (problema) { return; }
    const estudios = this.estudios.map(e => ({ idCarrera: e.idCarrera as number, idUniversidad: e.idUniversidad as number, semestre: e.semestre as number }));
    const redes = this.redes.map(r => ({ idRedSocial: r.idRedSocial as number, nombreUsuario: r.nombreUsuario.trim() }));
    const comun = {
      nombre: this.nombre.trim(),
      apellido: this.apellido.trim(),
      genero: (this.genero || GeneroEnum.NO_ESPECIFICA) as GeneroEnum,
      fechaNacimiento: this.fechaNacimiento || undefined,
      fotoPerfil: this.fotoPerfil.trim(),
      intereses: this.intereses(),
      estudios: this.esEstudiante ? estudios : [],
      redes
    };
    if (this.modo === 'edicion') {
      this.enviar.emit({ ...comun, fechaNacimiento: this.fechaNacimiento } as ActualizarPerfilRequestModel);
    } else if (this.modo === 'registro') {
      this.enviar.emit({
        ...comun, correo: this.correo.trim(), contrasena: this.contrasena,
        fechaNacimiento: this.fechaNacimiento
      } as RegistroRequestModel);
    } else {
      this.enviar.emit({
        ...comun, correo: this.correo.trim(), rol: this.rol,
        ...(this.contrasena ? { contrasena: this.contrasena } : {})
      } as AdminUsuarioRequestModel);
    }
  }
}
