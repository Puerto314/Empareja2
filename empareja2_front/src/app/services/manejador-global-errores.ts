import { ErrorHandler, Injectable, inject } from '@angular/core';
import { RegistroErroresService } from './registro-errores.service';

@Injectable()
export class ManejadorGlobalErrores implements ErrorHandler {
  private registro = inject(RegistroErroresService);

  handleError(error: unknown): void {
    const original = (error as { rejection?: unknown } | null)?.rejection ?? error;
    this.registro.excepcion(original);
  }
}
