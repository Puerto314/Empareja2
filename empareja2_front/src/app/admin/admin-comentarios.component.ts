import { Component, inject, signal } from '@angular/core';
import { ComentarioModel } from '../models/interaccion.model';
import { AdminService } from '../services/admin.service';
import { mensajeDeError } from '../utils/error.util';

// Moderación de comentarios privados: listar todos y eliminar (llave = autor + número).
@Component({
  standalone: false,
  selector: 'app-admin-comentarios',
  templateUrl: './admin-comentarios.component.html'
})
export class AdminComentariosComponent {
  private admin = inject(AdminService);

  comentarios = signal<ComentarioModel[]>([]);
  cargando = signal(true);
  error = signal('');
  exito = signal('');
  confirmando = signal('');

  constructor() { this.cargar(); }

  llave(c: ComentarioModel): string { return `${c.idAutor}/${c.numeroComentario}`; }

  cargar(): void {
    this.cargando.set(true);
    this.admin.listarComentarios().subscribe({
      next: l => { this.comentarios.set(l); this.cargando.set(false); },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar los comentarios.')); this.cargando.set(false); }
    });
  }

  pedirBorrado(c: ComentarioModel): void { this.confirmando.set(this.llave(c)); }
  cancelarBorrado(): void { this.confirmando.set(''); }

  eliminar(c: ComentarioModel): void {
    this.admin.eliminarComentario(c.idAutor, c.numeroComentario).subscribe({
      next: () => { this.confirmando.set(''); this.exito.set('Comentario eliminado.'); this.cargar(); },
      error: (err: unknown) => { this.confirmando.set(''); this.error.set(mensajeDeError(err, 'No pudimos eliminar el comentario.')); }
    });
  }
}
