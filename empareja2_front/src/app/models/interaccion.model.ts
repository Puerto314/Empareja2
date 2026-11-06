import { EstadoReporteEnum } from './estado-reporte.enum';
import { TipoReporteEnum } from './tipo-reporte.enum';

export interface MeGustaModel { idUsuario: number; totalMeGusta: number; meGusta: boolean; }

export interface ReaccionModel {
  idAutor: number; nombreAutor: string; fotoAutor: string | null; fechaHora: string;
}

export interface ReaccionAdminModel {
  numeroReaccion: number; idAutor: number; nombreAutor: string; idDestinatario: number;
  nombreDestinatario: string; fechaHora: string;
}

export interface ComentarioModel {
  numeroComentario: number; idAutor: number; nombreAutor: string; idDestinatario: number;
  nombreDestinatario: string; contenido: string; fechaHora: string;
}

export interface ReporteModel {
  idReporte: number; idRealizador: number; nombreRealizador: string; idReportado: number; nombreReportado: string;
  tipoReporte: TipoReporteEnum; estado: EstadoReporteEnum; descripcion: string | null; fechaHora: string;
}

export interface ReporteRequestModel { tipoReporte: TipoReporteEnum; descripcion?: string; }
