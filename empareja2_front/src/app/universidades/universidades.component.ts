import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { CatalogoService } from '../services/catalogo.service';
import { UsuarioService } from '../services/usuario.service';
import { UniversidadResumenModel } from '../models/universidad-resumen.model';
import { fondoAvatar, gradient, hueOf, inicialesAvatar } from '../utils/avatar.util';
import { mensajeDeError } from '../utils/error.util';

@Component({
  standalone: false,
  selector: 'app-universidades',
  templateUrl: './universidades.component.html',
  styleUrls: ['./universidades.component.css']
})
export class UniversidadesComponent {
  private catalogoService = inject(CatalogoService);
  private usuarioService = inject(UsuarioService);

  unis = signal<UniversidadResumenModel[]>([]);
  total = signal(0);
  cargando = signal(true);
  error = signal('');
  grad = gradient; hue = hueOf; fondo = fondoAvatar; ini = inicialesAvatar;

  constructor() {
    // El backend no cuenta estudiantes por universidad: se arma con el catálogo de universidades y el de estudiantes.
    forkJoin({ universidades: this.catalogoService.universidades(), usuarios: this.usuarioService.listarCatalogo() }).subscribe({
      next: ({ universidades, usuarios }) => {
        this.total.set(usuarios.length);
        this.unis.set(universidades.map(universidad => {
          const alumnos = usuarios.filter(u => u.estudios.some(e => e.idUniversidad === universidad.idUniversidad));
          return { universidad, cantidad: alumnos.length, vistaPrevia: alumnos.slice(0, 4) };
        }).sort((a, b) => b.cantidad - a.cantidad || a.universidad.nombre.localeCompare(b.universidad.nombre)));
        this.cargando.set(false);
      },
      error: err => {
        this.error.set(mensajeDeError(err, 'No pudimos cargar las universidades.'));
        this.cargando.set(false);
      }
    });
  }
}
