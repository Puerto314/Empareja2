import { Component, computed, inject, signal } from '@angular/core';
import { AdminUsuarioRequestModel } from '../models/perfil-request.model';
import { PerfilModel } from '../models/perfil.model';
import { PerfilFormValor } from '../perfil-form/perfil-form.component';
import { AdminService } from '../services/admin.service';
import { mensajeDeError } from '../utils/error.util';

// CRUD de usuarios (estudiantes y administradores): /api/admin/usuarios
@Component({
  standalone: false,
  selector: 'app-admin-usuarios',
  templateUrl: './admin-usuarios.component.html'
})
export class AdminUsuariosComponent {
  private admin = inject(AdminService);

  usuarios = signal<PerfilModel[]>([]);
  filtro = signal('');
  cargando = signal(true);
  error = signal('');
  exito = signal('');
  // null = formulario cerrado; 'nuevo' = crear; PerfilModel = editando
  formulario = signal<null | 'nuevo' | PerfilModel>(null);
  guardando = signal(false);
  errorForm = signal('');
  confirmandoId = signal<number | null>(null);

  visibles = computed(() => {
    const t = this.filtro().toLowerCase().trim();
    return this.usuarios().filter(u => !t || (u.nombreCompleto + ' ' + u.correo).toLowerCase().includes(t));
  });

  constructor() { this.cargar(); }

  universidadesDe(u: PerfilModel): string {
    const nombres = Array.from(new Set(u.estudios.map(e => e.siglaUniversidad || e.universidad)));
    return nombres.length ? nombres.join(', ') : '—';
  }

  cargar(): void {
    this.cargando.set(true);
    this.admin.listarUsuarios().subscribe({
      next: l => { this.usuarios.set(l); this.cargando.set(false); },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar los usuarios.')); this.cargando.set(false); }
    });
  }

  nuevo(): void { this.exito.set(''); this.errorForm.set(''); this.formulario.set('nuevo'); }

  // Trae el usuario fresco con GET /api/admin/usuarios/{id} antes de editarlo.
  editar(u: PerfilModel): void {
    this.exito.set(''); this.errorForm.set('');
    this.admin.obtenerUsuario(u.id).subscribe({
      next: fresco => this.formulario.set(fresco),
      error: (err: unknown) => this.error.set(mensajeDeError(err, 'No pudimos abrir el usuario.'))
    });
  }

  cancelar(): void { this.formulario.set(null); this.errorForm.set(''); }

  guardar(valor: PerfilFormValor): void {
    const actual = this.formulario();
    if (actual === null) { return; }
    this.guardando.set(true);
    this.errorForm.set('');
    const datos = valor as AdminUsuarioRequestModel;
    const peticion = actual === 'nuevo' ? this.admin.crearUsuario(datos) : this.admin.actualizarUsuario(actual.id, datos);
    peticion.subscribe({
      next: () => {
        this.guardando.set(false);
        this.formulario.set(null);
        this.exito.set(actual === 'nuevo' ? 'Usuario creado.' : 'Usuario actualizado.');
        this.cargar();
      },
      error: (err: unknown) => { this.errorForm.set(mensajeDeError(err, 'No pudimos guardar el usuario.')); this.guardando.set(false); }
    });
  }

  pedirBorrado(u: PerfilModel): void { this.confirmandoId.set(u.id); }
  cancelarBorrado(): void { this.confirmandoId.set(null); }

  eliminar(u: PerfilModel): void {
    this.admin.eliminarUsuario(u.id).subscribe({
      next: () => { this.confirmandoId.set(null); this.exito.set(`Usuario ${u.nombreCompleto} eliminado.`); this.error.set(''); this.cargar(); },
      error: (err: unknown) => { this.confirmandoId.set(null); this.error.set(mensajeDeError(err, 'No pudimos eliminar el usuario.')); }
    });
  }
}
