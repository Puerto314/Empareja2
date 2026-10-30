import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { PerfilComponent } from './perfil.component';
import { AuthService } from '../services/auth.service';
import { EstudianteService } from '../services/estudiante.service';
import { InteraccionService } from '../services/interaccion.service';

const estudio = { idCarrera: 1, carrera: 'Medicina', idUniversidad: 5, universidad: 'Universidad Uno', siglaUniversidad: 'UU', semestre: 2 };
const ana: any = {
  id: 1, nombre: 'Ana', apellido: 'Prueba', nombreCompleto: 'Ana Prueba', edad: 21, genero: 'FEMENINO', fotoPerfil: null,
  intereses: ['Cine', 'Rock'], redes: [{ idRedSocial: 1, redSocial: 'Instagram', urlPerfil: 'https://instagram.com/ana', alias: '@ana' }],
  estudios: [estudio], totalMeGusta: 3, meGustaDelUsuarioActual: false
};
const luis: any = { ...ana, id: 2, nombre: 'Luis', nombreCompleto: 'Luis Prueba', intereses: [], redes: [] };

describe('PerfilComponent', () => {
  let fixture: ComponentFixture<PerfilComponent>;
  let comp: PerfilComponent;
  let llamadas: string[] = [];
  let usuarioActual: any = { id: 99, rol: 'ESTUDIANTE' };

  const serviceMock: any = {};
  const interaccionMock: any = {};
  const authMock: any = { obtenerUsuario: () => usuarioActual, esAdmin: () => usuarioActual?.rol === 'ADMIN' };

  beforeEach(() => {
    llamadas = [];
    usuarioActual = { id: 99, rol: 'ESTUDIANTE' };
    serviceMock.obtenerPorId = () => of(ana);
    serviceMock.listarCompaneros = () => of([luis]);
    interaccionMock.listarMeGusta = () => of([{ idAutor: 50, nombreAutor: 'Marta Ruiz', fotoAutor: null, tipoReaccion: 'ME_GUSTA', fechaHora: '2026-10-01T10:00:00' }]);
    interaccionMock.darMeGusta = (id: number) => { llamadas.push('dar ' + id); return of({ idUsuario: id, totalMeGusta: 4, meGusta: true }); };
    interaccionMock.quitarMeGusta = (id: number) => { llamadas.push('quitar ' + id); return of({ idUsuario: id, totalMeGusta: 3, meGusta: false }); };
    interaccionMock.enviarComentario = (id: number, t: string) => { llamadas.push(`comentario ${id} ${t}`); return of({}); };
    interaccionMock.reportar = (id: number, d: any) => { llamadas.push(`reporte ${id} ${d.tipoReporte} ${d.descripcion ?? ''}`); return of({}); };
  });

  const crear = async () => {
    await TestBed.configureTestingModule({
      declarations: [PerfilComponent],
      imports: [FormsModule],
      providers: [
        { provide: EstudianteService, useValue: serviceMock },
        { provide: InteraccionService, useValue: interaccionMock },
        { provide: AuthService, useValue: authMock },
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id: '1' })) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(PerfilComponent);
    comp = fixture.componentInstance;
    fixture.detectChanges();
  };

  const texto = () => fixture.nativeElement.textContent as string;
  const clic = (sel: string) => { (fixture.nativeElement.querySelector(sel) as HTMLElement).click(); fixture.detectChanges(); };

  it('debe crearse', async () => {
    await crear();
    expect(comp).toBeTruthy();
  });

  it('debe mostrar el nombre del estudiante', async () => {
    await crear();
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Ana Prueba');
  });

  it('debe mostrar gustos, edad, género y datos académicos', async () => {
    await crear();
    expect(texto()).toContain('Cine');
    expect(texto()).toContain('21 años');
    expect(texto()).toContain('Femenino');
    expect(texto()).toContain('Medicina');
  });

  it('debe mostrar las redes sociales como enlaces externos seguros', async () => {
    await crear();
    const enlace = fixture.nativeElement.querySelector('a.red') as HTMLAnchorElement;
    expect(enlace.href).toBe('https://instagram.com/ana');
    expect(enlace.rel).toContain('noopener');
    expect(enlace.target).toBe('_blank');
  });

  it('debe listar compañeros de la misma universidad', async () => {
    await crear();
    expect(texto()).toContain('Luis Prueba');
  });

  it('debe listar quién le dio me gusta', async () => {
    await crear();
    expect(texto()).toContain('Marta Ruiz');
  });

  it('debe dar me gusta y actualizar el contador', async () => {
    await crear();
    clic('button.like');
    expect(llamadas).toEqual(['dar 1']);
    expect(comp.meGusta()).toBe(true);
    expect(comp.totalMeGusta()).toBe(4);
  });

  it('debe quitar el me gusta si ya se lo había dado', async () => {
    serviceMock.obtenerPorId = () => of({ ...ana, meGustaDelUsuarioActual: true });
    await crear();
    clic('button.like');
    expect(llamadas).toEqual(['quitar 1']);
    expect(comp.meGusta()).toBe(false);
    expect(comp.totalMeGusta()).toBe(3);
  });

  it('debe mostrar el error si el me gusta falla (p. ej. ya lo había dado)', async () => {
    interaccionMock.darMeGusta = () => throwError(() => new HttpErrorResponse({ status: 409, error: { message: 'Ya le diste me gusta a este perfil' } }));
    await crear();
    clic('button.like');
    expect(texto()).toContain('Ya le diste me gusta a este perfil');
  });

  it('debe enviar un comentario privado', async () => {
    await crear();
    comp.comentario = '  Hola, qué buen perfil  ';
    comp.enviarComentario();
    expect(llamadas).toEqual(['comentario 1 Hola, qué buen perfil']);
    expect(comp.exitoComentario()).toContain('Solo Ana puede leerlo');
    expect(comp.comentario).toBe('');
  });

  it('no debe enviar un comentario vacío', async () => {
    await crear();
    comp.comentario = '   ';
    comp.enviarComentario();
    expect(llamadas).toEqual([]);
    expect(comp.errorComentario()).toContain('Escribe el comentario');
  });

  it('debe reportar el perfil con el motivo elegido', async () => {
    await crear();
    comp.alternarReporte();
    comp.tipoReporte = 'ACOSO' as any;
    comp.enviarReporte();
    expect(llamadas).toEqual(['reporte 1 ACOSO ']);
    expect(comp.exitoReporte()).toContain('Reporte enviado');
  });

  it('debe exigir descripción cuando el motivo es OTRO', async () => {
    await crear();
    comp.tipoReporte = 'OTRO' as any;
    comp.descripcionReporte = ' ';
    comp.enviarReporte();
    expect(llamadas).toEqual([]);
    expect(comp.errorReporte()).toContain('Describe el motivo');
  });

  it('debe mostrar el mensaje del back si el reporte ya existe (409)', async () => {
    interaccionMock.reportar = () => throwError(() => new HttpErrorResponse({ status: 409, error: { message: 'Ya reportaste este perfil' } }));
    await crear();
    comp.enviarReporte();
    expect(comp.errorReporte()).toContain('Ya reportaste este perfil');
  });

  it('no debe ofrecer me gusta, comentario ni reporte en el perfil propio', async () => {
    usuarioActual = { id: 1, rol: 'ESTUDIANTE' };
    await crear();
    expect(fixture.nativeElement.querySelector('button.like')).toBeNull();
    expect(texto()).not.toContain('Escríbele en privado');
    expect(texto()).toContain('Editar mi perfil');
  });

  it('no debe ofrecer interacciones a un administrador', async () => {
    usuarioActual = { id: 3, rol: 'ADMIN' };
    await crear();
    expect(fixture.nativeElement.querySelector('button.like')).toBeNull();
    expect(texto()).not.toContain('Reportar este perfil');
  });

  it('debe avisar si el perfil no existe', async () => {
    serviceMock.obtenerPorId = () => of(undefined);
    await crear();
    expect(texto()).toContain('No encontramos este perfil');
  });

  it('debe mostrar un mensaje si el servidor falla', async () => {
    serviceMock.obtenerPorId = () => throwError(() => new Error('falló'));
    await crear();
    expect(texto()).toContain('No pudimos cargar este perfil');
  });
});
