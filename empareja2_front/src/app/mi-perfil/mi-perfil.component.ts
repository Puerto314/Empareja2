import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ActualizarPerfilRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { PerfilFormValor } from '../perfil-form/perfil-form.component';
import { CuentaService } from '../services/cuenta.service';
import { mensajeDeError } from '../utils/error.util';
import { gradient, hueOf, initials } from '../utils/avatar.util';

// Perfil propio: ver, editar (PUT /usuarios/me) y eliminar la cuenta (DELETE /usuarios/me).
@Component({
  standalone: false,
  selector: 'app-mi-perfil',
  templateUrl: './mi-perfil.component.html',
  styleUrls: ['./mi-perfil.component.css']
})
export class MiPerfilComponent {
  private cuenta = inject(CuentaService);
  private router = inject(Router);

  perfil = signal<PerfilModel | undefined>(undefined);
  cargando = signal(true);
  error = signal('');
  editando = signal(false);
  guardando = signal(false);
  errorGuardar = signal('');
  exito = signal('');
  confirmandoBorrado = signal(false);
  grad = gradient; hue = hueOf; ini = initials;

  constructor() { this.cargar(); }

  private cargar(): void {
    this.cargando.set(true);
    this.cuenta.miPerfil().subscribe({
      next: p => { this.perfil.set(p); this.cargando.set(false); },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar tu perfil.')); this.cargando.set(false); }
    });
  }

  editar(): void { this.exito.set(''); this.errorGuardar.set(''); this.editando.set(true); }
  cancelarEdicion(): void { this.editando.set(false); this.errorGuardar.set(''); }

  guardar(valor: PerfilFormValor): void {
    this.guardando.set(true);
    this.errorGuardar.set('');
    this.cuenta.actualizar(valor as ActualizarPerfilRequestModel).subscribe({
      next: p => {
        this.perfil.set(p);
        this.guardando.set(false);
        this.editando.set(false);
        this.exito.set('Tu perfil se actualizó correctamente.');
      },
      error: (err: unknown) => {
        this.errorGuardar.set(mensajeDeError(err, 'No pudimos guardar los cambios.'));
        this.guardando.set(false);
      }
    });
  }

  pedirBorrado(): void { this.confirmandoBorrado.set(true); }
  cancelarBorrado(): void { this.confirmandoBorrado.set(false); }

  eliminarCuenta(): void {
    this.cuenta.eliminar().subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err: unknown) => {
        this.errorGuardar.set(mensajeDeError(err, 'No pudimos eliminar tu cuenta.'));
        this.confirmandoBorrado.set(false);
      }
    });
  }
}
