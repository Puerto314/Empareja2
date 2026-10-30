import { Component, inject, signal } from '@angular/core';
import { EstadoReporteEnum } from '../models/estado-reporte.enum';
import { ReporteModel } from '../models/interaccion.model';
import { AdminService } from '../services/admin.service';
import { mensajeDeError } from '../utils/error.util';

// Moderación de reportes: listar (con filtro por estado), ver, cambiar estado y eliminar.
@Component({
  standalone: false,
  selector: 'app-admin-reportes',
  templateUrl: './admin-reportes.component.html'
})
export class AdminReportesComponent {
  private admin = inject(AdminService);

  readonly estados = Object.values(EstadoReporteEnum);
  reportes = signal<ReporteModel[]>([]);
  filtro = signal<EstadoReporteEnum | ''>('');
  detalle = signal<ReporteModel | null>(null);
  cargando = signal(true);
  error = signal('');
  exito = signal('');
  confirmandoId = signal<number | null>(null);

  constructor() { this.cargar(); }

  cargar(): void {
    this.cargando.set(true);
    this.admin.listarReportes(this.filtro() || undefined).subscribe({
      next: l => { this.reportes.set(l); this.cargando.set(false); },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cargar los reportes.')); this.cargando.set(false); }
    });
  }

  filtrar(valor: string): void { this.filtro.set(valor as EstadoReporteEnum | ''); this.cargar(); }

  ver(r: ReporteModel): void {
    this.admin.obtenerReporte(r.idReporte).subscribe({
      next: d => this.detalle.set(d),
      error: (err: unknown) => this.error.set(mensajeDeError(err, 'No pudimos abrir el reporte.'))
    });
  }

  cambiarEstado(r: ReporteModel, estado: string): void {
    this.error.set(''); this.exito.set('');
    this.admin.actualizarReporte(r.idReporte, estado as EstadoReporteEnum).subscribe({
      next: nuevo => {
        this.reportes.update(l => l.map(x => (x.idReporte === nuevo.idReporte ? nuevo : x)));
        if (this.detalle()?.idReporte === nuevo.idReporte) { this.detalle.set(nuevo); }
        this.exito.set(`Reporte #${nuevo.idReporte} marcado como ${nuevo.estado}.`);
      },
      error: (err: unknown) => { this.error.set(mensajeDeError(err, 'No pudimos cambiar el estado.')); this.cargar(); }
    });
  }

  pedirBorrado(r: ReporteModel): void { this.confirmandoId.set(r.idReporte); }
  cancelarBorrado(): void { this.confirmandoId.set(null); }

  eliminar(r: ReporteModel): void {
    this.admin.eliminarReporte(r.idReporte).subscribe({
      next: () => {
        this.confirmandoId.set(null);
        if (this.detalle()?.idReporte === r.idReporte) { this.detalle.set(null); }
        this.exito.set(`Reporte #${r.idReporte} eliminado.`);
        this.cargar();
      },
      error: (err: unknown) => { this.confirmandoId.set(null); this.error.set(mensajeDeError(err, 'No pudimos eliminar el reporte.')); }
    });
  }

  claseEstado(e: EstadoReporteEnum): string {
    return e === EstadoReporteEnum.RESUELTO ? 'ok' : e === EstadoReporteEnum.PENDIENTE ? 'warn' : e === EstadoReporteEnum.DESCARTADO ? '' : 'bad';
  }
}
