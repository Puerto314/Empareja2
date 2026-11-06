import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../config/api.config';
import { mensajeDeError } from '../utils/error.util';

const PREFIJO = '[empareja2]';

@Injectable({ providedIn: 'root' })
export class RegistroErroresService {
  private registrados = new WeakSet<object>();

  http(err: HttpErrorResponse, metodo = 'GET', url: string = err.url ?? ''): void {
    if (this.yaRegistrado(err)) { return; }
    const ruta = url.startsWith(API_URL) ? url.slice(API_URL.length) || '/' : url || 'desconocida';
    const estado = err.status === 0 ? 'sin respuesta' : String(err.status);
    const mensaje = mensajeDeError(err, err.statusText || 'Error HTTP');
    const linea = `${PREFIJO} HTTP ${estado} ${metodo} ${ruta} -> ${mensaje}`;
    if (err.status === 0 || err.status >= 500) {
      console.error(linea);
    } else {
      console.warn(linea);
    }
  }

  excepcion(error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      this.http(error);
      return;
    }
    if (this.yaRegistrado(error)) { return; }
    if (error instanceof Error) {
      console.error(`${PREFIJO} Excepción no controlada: ${error.name}: ${error.message}`, error.stack ?? '');
      return;
    }
    console.error(`${PREFIJO} Excepción no controlada: ${this.describir(error)}`);
  }

  private yaRegistrado(valor: unknown): boolean {
    if (typeof valor !== 'object' || valor === null) { return false; }
    if (this.registrados.has(valor)) { return true; }
    this.registrados.add(valor);
    return false;
  }

  private describir(valor: unknown): string {
    if (typeof valor === 'string') { return valor; }
    try {
      return JSON.stringify(valor) ?? String(valor);
    } catch {
      return String(valor);
    }
  }
}
