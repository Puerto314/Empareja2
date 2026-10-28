import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { EstudiantesComponent } from './estudiantes.component';
import { CatalogoService } from '../services/catalogo.service';
import { UsuarioService } from '../services/usuario.service';
import { OTRO_USUARIO_EJEMPLO, UNIVERSIDAD_EJEMPLO, USUARIO_EJEMPLO } from '../testing/fixtures';

describe('EstudiantesComponent', () => {
  let idPedido: number | undefined;

  const crear = async (usuarios$: any = of([USUARIO_EJEMPLO, OTRO_USUARIO_EJEMPLO])) => {
    await TestBed.configureTestingModule({
      declarations: [EstudiantesComponent],
      providers: [
        { provide: CatalogoService, useValue: { universidades: () => of([UNIVERSIDAD_EJEMPLO]) } },
        { provide: UsuarioService, useValue: { listarCatalogo: (id: number) => { idPedido = id; return usuarios$; } } },
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id: '1' })) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    const fixture: ComponentFixture<EstudiantesComponent> = TestBed.createComponent(EstudiantesComponent);
    fixture.detectChanges();
    return fixture;
  };

  beforeEach(() => { idPedido = undefined; });

  it('debe crearse y pedir los estudiantes de la universidad de la ruta', async () => {
    const c = (await crear()).componentInstance;
    expect(c).toBeTruthy();
    expect(idPedido).toBe(1);
    expect(c.nombre()).toBe('Universidad Uno');
  });

  it('debe mostrar una tarjeta por estudiante', async () => {
    const fixture = await crear();
    expect(fixture.nativeElement.querySelectorAll('.card').length).toBe(2);
  });

  it('debe filtrar por texto de búsqueda', async () => {
    const c = (await crear()).componentInstance;
    c.q.set('medicina');
    expect(c.list().map(s => s.id)).toEqual([1]);
  });

  it('debe filtrar por rango de semestre', async () => {
    const c = (await crear()).componentInstance;
    c.rango.set('c');
    expect(c.list().map(s => s.id)).toEqual([2]);
  });

  it('debe mostrar el error si el backend falla', async () => {
    const c = (await crear(throwError(() => ({ status: 404, error: { message: 'No existe la universidad' } })))).componentInstance;
    expect(c.error()).toBe('No existe la universidad');
    expect(c.cargando()).toBe(false);
  });
});
