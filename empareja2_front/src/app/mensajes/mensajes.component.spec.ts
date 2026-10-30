import { TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { MensajesComponent } from './mensajes.component';
import { InteraccionService } from '../services/interaccion.service';

const c = (autor: number, num: number, texto: string): any => ({
  numeroComentario: num, idAutor: autor, nombreAutor: 'Autor' + autor, idDestinatario: 9, nombreDestinatario: 'Dest', contenido: texto, fechaHora: '2026-10-01T10:00:00'
});

describe('MensajesComponent', () => {
  const servicio: any = {};
  beforeEach(async () => {
    servicio.comentariosRecibidos = () => of([c(2, 1, 'Hola recibido')]);
    servicio.comentariosEnviados = () => of([c(1, 1, 'Hola enviado'), c(1, 2, 'Otro')]);
    await TestBed.configureTestingModule({
      declarations: [MensajesComponent], providers: [{ provide: InteraccionService, useValue: servicio }], schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  it('debe mostrar recibidos y cambiar a enviados', () => {
    const f = TestBed.createComponent(MensajesComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('Hola recibido');
    expect(f.nativeElement.textContent).toContain('Recibidos (1)');
    f.componentInstance.pestana.set('enviados');
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('Hola enviado');
    expect(f.nativeElement.querySelectorAll('.comentario').length).toBe(2);
  });

  it('debe mostrar mensaje si no hay comentarios', () => {
    servicio.comentariosRecibidos = () => of([]);
    const f = TestBed.createComponent(MensajesComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('Todavía no has recibido comentarios');
  });

  it('debe mostrar error si falla', () => {
    servicio.comentariosEnviados = () => throwError(() => new Error('x'));
    const f = TestBed.createComponent(MensajesComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('No pudimos cargar tus comentarios');
  });
});
