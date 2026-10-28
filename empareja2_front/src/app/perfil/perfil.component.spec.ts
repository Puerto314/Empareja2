import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { PerfilComponent } from './perfil.component';
import { UsuarioService } from '../services/usuario.service';
import { OTRO_USUARIO_EJEMPLO, USUARIO_EJEMPLO } from '../testing/fixtures';

describe('PerfilComponent', () => {
  let fixture: ComponentFixture<PerfilComponent>;
  let component: PerfilComponent;
  const serviceMock: any = {};

  const crear = async () => {
    await TestBed.configureTestingModule({
      declarations: [PerfilComponent],
      providers: [
        { provide: UsuarioService, useValue: serviceMock },
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id: '1' })) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(PerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  };

  beforeEach(() => {
    serviceMock.obtenerPublico = () => of(USUARIO_EJEMPLO);
    serviceMock.listarCatalogo = () => of([USUARIO_EJEMPLO, OTRO_USUARIO_EJEMPLO]);
    serviceMock.darMeGusta = () => of({ idUsuario: 1, totalMeGusta: 4, meGusta: true });
    serviceMock.quitarMeGusta = () => of({ idUsuario: 1, totalMeGusta: 2, meGusta: false });
  });

  it('debe crearse y mostrar el nombre', async () => {
    await crear();
    expect(component).toBeTruthy();
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Ana Prueba');
  });

  /* PRUEBAS A y B: descomenta estos tests cuando actives el me gusta y los compañeros
  it('debe listar compañeros sin incluir al propio perfil', async () => {
    await crear();
    expect(component.otros().map(o => o.id)).toEqual([2]);
    expect(fixture.nativeElement.querySelectorAll('.mini-row').length).toBe(1);
  });

  it('debe dar me gusta y actualizar el contador', async () => {
    await crear();
    component.alternarMeGusta();
    expect(component.u()?.totalMeGusta).toBe(4);
    expect(component.u()?.meGustaDelUsuarioActual).toBe(true);
  });

  it('debe quitar el me gusta si ya estaba dado', async () => {
    serviceMock.obtenerPublico = () => of({ ...USUARIO_EJEMPLO, totalMeGusta: 3, meGustaDelUsuarioActual: true });
    await crear();
    component.alternarMeGusta();
    expect(component.u()?.totalMeGusta).toBe(2);
    expect(component.u()?.meGustaDelUsuarioActual).toBe(false);
  });

  fin PRUEBAS A y B */

  it('debe mostrar el error si el perfil no existe', async () => {
    serviceMock.obtenerPublico = () => throwError(() => ({ status: 404, error: { message: 'No existe el usuario' } }));
    await crear();
    expect(component.error()).toBe('No existe el usuario');
  });
});
