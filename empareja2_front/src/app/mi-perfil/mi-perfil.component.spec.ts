import { TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { MiPerfilComponent } from './mi-perfil.component';
import { CuentaService } from '../services/cuenta.service';

const perfil: any = {
  id: 1, correo: 'ana@x.co', rol: 'ESTUDIANTE', tipoDocumento: 'CC', documento: '123456', genero: null, nombre: 'Ana', apellido: 'Gil',
  nombreCompleto: 'Ana Gil', edad: 25, fechaNacimiento: '2001-01-01', fotoPerfil: null, intereses: ['Cine'], estudios: [], redes: [], fechaCreacion: '2026-01-01T00:00:00'
};

describe('MiPerfilComponent', () => {
  let ruta: string[] = [];
  let enviado: any = null;
  let eliminado = false;
  const cuenta: any = {};

  beforeEach(async () => {
    ruta = []; enviado = null; eliminado = false;
    cuenta.miPerfil = () => of(perfil);
    cuenta.actualizar = (d: unknown) => { enviado = d; return of({ ...perfil, nombre: 'Anita', nombreCompleto: 'Anita Gil' }); };
    cuenta.eliminar = () => { eliminado = true; return of(undefined); };
    await TestBed.configureTestingModule({
      declarations: [MiPerfilComponent],
      providers: [{ provide: CuentaService, useValue: cuenta }, { provide: Router, useValue: { navigate: (r: string[]) => { ruta = r; } } }],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  it('debe cargar y mostrar el perfil propio con sus datos privados', () => {
    const f = TestBed.createComponent(MiPerfilComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('Ana Gil');
    expect(f.nativeElement.textContent).toContain('ana@x.co');
    expect(f.nativeElement.textContent).toContain('123456');
  });

  it('debe guardar los cambios con PUT y mostrar el perfil actualizado', () => {
    const f = TestBed.createComponent(MiPerfilComponent);
    f.detectChanges();
    const c = f.componentInstance;
    c.editar();
    c.guardar({ nombre: 'Anita' } as any);
    expect(enviado).toEqual({ nombre: 'Anita' });
    expect(c.perfil()?.nombre).toBe('Anita');
    expect(c.editando()).toBe(false);
    expect(c.exito()).toContain('actualizó');
  });

  it('debe mostrar el error del back al guardar', () => {
    cuenta.actualizar = () => throwError(() => new HttpErrorResponse({ status: 400, error: { message: 'Debes tener al menos 18 anos para registrarte' } }));
    const c = TestBed.createComponent(MiPerfilComponent).componentInstance;
    c.guardar({} as any);
    expect(c.errorGuardar()).toContain('al menos 18');
    expect(c.guardando()).toBe(false);
  });

  it('eliminar cuenta pide confirmación y luego envía al login', () => {
    const c = TestBed.createComponent(MiPerfilComponent).componentInstance;
    c.pedirBorrado();
    expect(c.confirmandoBorrado()).toBe(true);
    expect(eliminado).toBe(false);
    c.eliminarCuenta();
    expect(eliminado).toBe(true);
    expect(ruta).toEqual(['/login']);
  });

  it('cancelar el borrado no elimina nada', () => {
    const c = TestBed.createComponent(MiPerfilComponent).componentInstance;
    c.pedirBorrado();
    c.cancelarBorrado();
    expect(c.confirmandoBorrado()).toBe(false);
    expect(eliminado).toBe(false);
  });

  it('debe mostrar un mensaje si el perfil no carga', () => {
    cuenta.miPerfil = () => throwError(() => new HttpErrorResponse({ status: 500 }));
    const f = TestBed.createComponent(MiPerfilComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('No pudimos cargar tu perfil');
  });
});
