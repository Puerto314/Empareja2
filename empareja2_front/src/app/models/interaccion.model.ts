import { EstadoReporteEnum } from './estado-reporte.enum';
import { TipoReaccionEnum } from './tipo-reaccion.enum';
import { TipoReporteEnum } from './tipo-reporte.enum';

// MeGustaDTO: estado de los "me gusta" de un perfil tras darlo o quitarlo.
export interface MeGustaModel { idUsuario: number; totalMeGusta: number; meGusta: boolean; }

// ReaccionDTO: una reacción pública (quién la dio y cuándo).
export interface ReaccionModel {
  idAutor: number; nombreAutor: string; fotoAutor: string | null; tipoReaccion: TipoReaccionEnum; fechaHora: string;
}

// ReaccionAdminDTO: vista de moderación (llave = idAutor + numeroReaccion).
export interface ReaccionAdminModel {
  numeroReaccion: number; idAutor: number; nombreAutor: string; idDestinatario: number;
  nombreDestinatario: string; tipoReaccion: TipoReaccionEnum; fechaHora: string;
}

// ComentarioDTO: comentario privado (llave = idAutor + numeroComentario).
export interface ComentarioModel {
  numeroComentario: number; idAutor: number; nombreAutor: string; idDestinatario: number;
  nombreDestinatario: string; contenido: string; fechaHora: string;
}

// ReporteDTO.
export interface ReporteModel {
  idReporte: number; idRealizador: number; nombreRealizador: string; idReportado: number; nombreReportado: string;
  tipoReporte: TipoReporteEnum; estado: EstadoReporteEnum; descripcion: string | null; fechaHora: string;
}

// ReporteRequestDTO.
export interface ReporteRequestModel { tipoReporte: TipoReporteEnum; descripcion?: string; }
