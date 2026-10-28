import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { UniversidadesComponent } from './universidades.component';
import { CatalogoService } from '../services/catalogo.service';
import { UsuarioService } from '../services/usuario.service';
import { OTRO_USUARIO_EJEMPLO, UNIVERSIDAD_EJEMPLO, UNIVERSIDAD_VACIA_EJEMPLO, USUARIO_EJEMPLO } from '../testing/fixtures';

describe('UniversidadesComponent', () => {
  const crear = async (usuarios$: any = of([USUARIO_EJEMPLO, OTRO_USUARIO_EJEMPLO])) => {
    await TestBed.configureTestingModule({
      declarations: [UniversidadesComponent],
      providers: [
        { provide: CatalogoService, useValue: { universidades: () => of([UNIVERSIDAD_VACIA_EJEMPLO, UNIVERSIDAD_EJEMPLO]) } },
        { provide: UsuarioService, useValue: { listarCatalogo: () => usuarios$ } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    const fixture: ComponentFixture<UniversidadesComponent> = TestBed.createComponent(UniversidadesComponent);
    fixture.detectChanges();
    return fixture;
  };

  it('debe crearse', async () => {
    expect((await crear()).componentInstance).toBeTruthy();
  });

  it('debe contar estudiantes por universidad y poner primero la que más tiene', async () => {
    const c = (await crear()).componentInstance;
    expect(c.total()).toBe(2);
    expect(c.unis().map(r => r.universidad.nombre)).toEqual(['Universidad Uno', 'Universidad Dos']);
    expect(c.unis().map(r => r.cantidad)).toEqual([2, 0]);
  });

  it('debe mostrar una tarjeta por universidad', async () => {
    const fixture = await crear();
    expect(fixture.nativeElement.querySelectorAll('.tile').length).toBe(2);
  });

  it('debe mostrar el error si el backend falla', async () => {
    const fixture = await crear(throwError(() => ({ status: 500, error: { message: 'Fallo del servidor' } })));
    expect(fixture.componentInstance.error()).toBe('Fallo del servidor');
    expect(fixture.componentInstance.cargando()).toBe(false);
  });
});
