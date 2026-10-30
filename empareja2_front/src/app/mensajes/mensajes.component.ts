import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ComentarioModel } from '../models/interaccion.model';
import { InteraccionService } from '../services/interaccion.service';
import { mensajeDeError } from '../utils/error.util';

// Comentarios privados: los que me enviaron y los que yo envié.
@Component({
  standalone: false,
  selector: 'app-mensajes',
  templateUrl: './mensajes.component.html',
  styleUrls: ['./mensajes.component.css']
})
export class MensajesComponent {
  private interaccion = inject(InteraccionService);

  recibidos = signal<ComentarioModel[]>([]);
  enviados = signal<ComentarioModel[]>([]);
  pestana = signal<'recibidos' | 'enviados'>('recibidos');
  cargando = signal(true);
  error = signal('');

  constructor() {
    forkJoin({ r: this.interaccion.comentariosRecibidos(), e: this.interaccion.comentariosEnviados() }).subscribe({
      next: ({ r, e }) => { this.recibidos.set(r); this.enviados.set(e); this.cargando.set(false); },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar tus comentarios.')); this.cargando.set(false); }
    });
  }
}
