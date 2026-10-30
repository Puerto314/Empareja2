import { Component, inject, signal } from '@angular/core';
import { ReaccionAdminModel } from '../models/interaccion.model';
import { AdminService } from '../services/admin.service';
import { mensajeDeError } from '../utils/error.util';

// Moderación de me gusta: listar todos y eliminar (llave = autor + número).
@Component({
  standalone: false,
  selector: 'app-admin-megusta',
  templateUrl: './admin-megusta.component.html'
})
export class AdminMeGustaComponent {
  private admin = inject(AdminService);

  reacciones = signal<ReaccionAdminModel[]>([]);
  cargando = signal(true);
  error = signal('');
  exito = signal('');
  confirmando = signal('');

  constructor() { this.cargar(); }

  llave(r: ReaccionAdminModel): string { return `${r.idAutor}/${r.numeroReaccion}`; }

  cargar(): void {
    this.cargando.set(true);
    this.admin.listarMeGusta().subscribe({
      next: l => { this.reacciones.set(l); this.cargando.set(false); },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar los me gusta.')); this.cargando.set(false); }
    });
  }

  pedirBorrado(r: ReaccionAdminModel): void { this.confirmando.set(this.llave(r)); }
  cancelarBorrado(): void { this.confirmando.set(''); }

  eliminar(r: ReaccionAdminModel): void {
    this.admin.eliminarMeGusta(r.idAutor, r.numeroReaccion).subscribe({
      next: () => { this.confirmando.set(''); this.exito.set('Me gusta eliminado.'); this.cargar(); },
      error: (err: unknown) => { this.confirmando.set(''); this.error.set(mensajeDeError(err, 'No pudimos eliminar el me gusta.')); }
    });
  }
}
