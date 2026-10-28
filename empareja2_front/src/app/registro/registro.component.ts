import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { CatalogoService } from '../services/catalogo.service';
import { CarreraModel } from '../models/carrera.model';
import { GeneroEnum } from '../models/genero.enum';
import { RegistroRequestModel } from '../models/registro-request.model';
import { TipoDocumentoEnum } from '../models/tipo-documento.enum';
import { UniversidadModel } from '../models/universidad.model';
import { mensajeDeError } from '../utils/error.util';

@Component({
  standalone: false,
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  private router = inject(Router);
  private authService = inject(AuthService);
  private catalogoService = inject(CatalogoService);

  universidades = signal<UniversidadModel[]>([]);
  carreras = signal<CarreraModel[]>([]);
  error = signal('');
  cargando = signal(false);

  tiposDocumento = Object.values(TipoDocumentoEnum);
  generos = [
    { valor: GeneroEnum.FEMENINO, texto: 'Femenino' },
    { valor: GeneroEnum.MASCULINO, texto: 'Masculino' },
    { valor: GeneroEnum.OTRO, texto: 'Otro' },
    { valor: GeneroEnum.NO_ESPECIFICA, texto: 'Prefiero no decirlo' }
  ];

  f = {
    correo: '', contrasena: '', nombre: '', apellido: '',
    tipoDocumento: TipoDocumentoEnum.CC, documento: '',
    genero: GeneroEnum.NO_ESPECIFICA, fechaNacimiento: '', fotoPerfil: '', intereses: '',
    idUniversidad: null as number | null, idCarrera: null as number | null, semestre: null as number | null
  };

  constructor() {
    forkJoin({ u: this.catalogoService.universidades(), c: this.catalogoService.carreras() }).subscribe({
      next: r => { this.universidades.set(r.u); this.carreras.set(r.c); },
      error: err => this.error.set(mensajeDeError(err, 'No pudimos cargar las universidades y carreras.'))
    });
  }

  registrar(): void {
    const f = this.f;
    if (!f.correo.trim() || !f.nombre.trim() || !f.apellido.trim() || !f.documento.trim() || !f.fechaNacimiento
      || f.idUniversidad === null || f.idCarrera === null || f.semestre === null) {
      this.error.set('Completa todos los campos obligatorios.');
      return;
    }
    if (f.contrasena.length < 8) {
      this.error.set('La contraseña debe tener al menos 8 caracteres.');
      return;
    }

    const datos: RegistroRequestModel = {
      correo: f.correo.trim(),
      contrasena: f.contrasena,
      tipoDocumento: f.tipoDocumento,
      documento: f.documento.trim(),
      genero: f.genero,
      nombre: f.nombre.trim(),
      apellido: f.apellido.trim(),
      fechaNacimiento: f.fechaNacimiento,
      fotoPerfil: f.fotoPerfil.trim() || null,
      intereses: Array.from(new Set(f.intereses.split(',').map(i => i.trim()).filter(Boolean))),
      estudios: [{ idCarrera: f.idCarrera, idUniversidad: f.idUniversidad, semestre: f.semestre }]
    };

    this.error.set('');
    this.cargando.set(true);
    this.authService.registrar(datos).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: err => {
        this.error.set(mensajeDeError(err, 'No pudimos crear tu cuenta.'));
        this.cargando.set(false);
      }
    });
  }
}
