import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { OfertaModel } from '../models/catalogo.model';
import { GeneroEnum } from '../models/genero.enum';
import { ActualizarPerfilRequestModel, AdminUsuarioRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { RegistroRequestModel } from '../models/registro-request.model';
import { RolEnum } from '../models/rol.enum';
import { UniversidadModel } from '../models/universidad.model';
import { CatalogoService } from '../services/catalogo.service';
import { UniversidadService } from '../services/universidad.service';
import { mensajeDeError } from '../utils/error.util';

export type ModoPerfilForm = 'registro' | 'edicion' | 'admin';
export type PerfilFormValor = RegistroRequestModel | ActualizarPerfilRequestModel | AdminUsuarioRequestModel;

interface FilaEstudio { idUniversidad: number | null; idCarrera: number | null; semestre: number | null; }
interface FilaRed { tipoRedSocial: string; urlPerfil: string; }

const EDAD_MINIMA = 18;

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
  readonly sugerenciasRed = ['Instagram', 'Facebook', 'X', 'TikTok', 'LinkedIn', 'GitHub', 'Snapchat', 'Discord'];
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
      oferta: this.catalogo.oferta()
    }).subscribe({
      next: r => {
        this.universidades.set(r.universidades);
        this.oferta.set(r.oferta);
      },
      error: (err: unknown) => this.errorCatalogo.set(
        mensajeDeError(err, 'No pudimos cargar universidades y carreras. Recarga la página.'))
    });
    this.asegurarFilaEstudio();
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
    this.redes = p.redes.map(r => ({ tipoRedSocial: r.tipoRedSocial, urlPerfil: r.urlPerfil }));
    this.asegurarFilaEstudio();
  }

  /** Un estudiante siempre necesita su fila de universidad, carrera y semestre. */
  private asegurarFilaEstudio(): void {
    if (this.esEstudiante && this.estudios.length === 0) { this.agregarEstudio(); }
  }

  /** Al cambiar el rol en el modo admin se muestran u ocultan los campos de estudiante. */
  cambiarRol(): void { this.asegurarFilaEstudio(); }

  carrerasDe(idUniversidad: number | null): OfertaModel[] {
    return idUniversidad === null ? [] : this.oferta().filter(o => o.idUniversidad === idUniversidad);
  }

  cambiarUniversidad(fila: FilaEstudio): void { fila.idCarrera = null; }
  agregarEstudio(): void { this.estudios.push({ idUniversidad: null, idCarrera: null, semestre: null }); }
  agregarRed(): void { this.redes.push({ tipoRedSocial: '', urlPerfil: '' }); }
  quitarRed(i: number): void { this.redes.splice(i, 1); }

  private intereses(): string[] {
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
    if (!this.nombre.trim()) { return this.modo === 'admin' ? 'Escribe el nombre.' : 'Escribe tu nombre.'; }
    if (!this.apellido.trim()) { return this.modo === 'admin' ? 'Escribe el apellido.' : 'Escribe tu apellido.'; }
    if (this.esEstudiante && !this.fechaNacimiento) {
      return this.modo === 'admin' ? 'Indica la fecha de nacimiento.' : 'Indica tu fecha de nacimiento.';
    }
    if (this.fechaNacimiento) {
      if (this.edadDe(this.fechaNacimiento) < 0) { return 'La fecha de nacimiento debe ser en el pasado.'; }
      if (this.edadDe(this.fechaNacimiento) < EDAD_MINIMA) { return `Debes tener al menos ${EDAD_MINIMA} años.`; }
    }
    if (this.esEstudiante && this.fotoPerfil.trim() && !/^https?:\/\/\S+$/.test(this.fotoPerfil.trim())) {
      return 'La foto de perfil debe ser un enlace que empiece por http:// o https://.';
    }
    if (this.esEstudiante) {
      if (this.estudios.length !== 1) {
        return this.modo === 'admin' ? 'Indica la universidad, carrera y semestre.' : 'Indica tu universidad, carrera y semestre.';
      }
      const e = this.estudios[0];
      if (e.idUniversidad === null || e.idCarrera === null || e.semestre === null) {
        return 'Completa universidad, carrera y semestre.';
      }
      if (e.semestre < 1 || e.semestre > 20) { return 'El semestre debe estar entre 1 y 20.'; }
    }
    const tipos = new Set<string>();
    for (const r of (this.esEstudiante ? this.redes : [])) {
      const tipo = r.tipoRedSocial.trim();
      const url = r.urlPerfil.trim();
      if (!tipo || !url) { return 'Cada red social necesita el tipo de red y el enlace a tu perfil.'; }
      if (tipo.length > 50) { return 'El tipo de red social admite máximo 50 caracteres.'; }
      if (url.length > 255) { return 'El enlace de una red social admite máximo 255 caracteres.'; }
      if (!/^https?:\/\/\S+$/i.test(url)) { return 'El enlace de cada red social debe empezar por http:// o https://.'; }
      if (tipos.has(tipo.toLowerCase())) { return 'No repitas la misma red social.'; }
      tipos.add(tipo.toLowerCase());
    }
    return '';
  }

  guardar(): void {
    const problema = this.validar();
    this.errorLocal.set(problema);
    if (problema) { return; }
    const esEst = this.esEstudiante;
    const estudios = this.estudios.map(e => ({ idCarrera: e.idCarrera as number, idUniversidad: e.idUniversidad as number, semestre: e.semestre as number }));
    const redes = this.redes.map(r => ({ tipoRedSocial: r.tipoRedSocial.trim(), urlPerfil: r.urlPerfil.trim() }));
    const foto = this.fotoPerfil.trim();
    // Sin foto no se envía el campo: el back guarda NULL en usuario.foto_perfil.
    // Un administrador nunca envía gustos, foto ni redes sociales.
    const comun = {
      nombre: this.nombre.trim(),
      apellido: this.apellido.trim(),
      genero: (this.genero || GeneroEnum.NO_ESPECIFICA) as GeneroEnum,
      fechaNacimiento: this.fechaNacimiento || undefined,
      ...(esEst && foto ? { fotoPerfil: foto } : {}),
      intereses: esEst ? this.intereses() : [],
      estudios: esEst ? estudios : [],
      redes: esEst ? redes : []
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
