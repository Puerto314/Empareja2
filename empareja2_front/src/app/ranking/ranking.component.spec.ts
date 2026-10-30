import { TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { BehaviorSubject, of, throwError } from 'rxjs';

import { RankingComponent } from './ranking.component';
import { EstudianteService } from '../services/estudiante.service';
import { UniversidadService } from '../services/universidad.service';

const est = (id: number, nombre: string, likes: number): any => ({
  id, nombre, apellido: 'X', nombreCompleto: nombre + ' X', edad: 20, genero: null, fotoPerfil: null, intereses: [], redes: [],
  totalMeGusta: likes, meGustaDelUsuarioActual: false,
  estudios: [{ idCarrera: 1, carrera: 'Medicina', idUniversidad: 1, universidad: 'Uni Uno', siglaUniversidad: 'U1', semestre: 2 }]
});

describe('RankingComponent', () => {
  let pedidos: (number | undefined)[] = [];
  let params$: BehaviorSubject<any>;
  let navegado: any = null;
  const servicio: any = {};

  beforeEach(async () => {
    pedidos = []; navegado = null;
    params$ = new BehaviorSubject(convertToParamMap({}));
    servicio.ranking = (id?: number) => { pedidos.push(id); return of([est(3, 'Marta', 9), est(2, 'Luis', 4)]); };
    await TestBed.configureTestingModule({
      declarations: [RankingComponent],
      imports: [FormsModule],
      providers: [
        { provide: EstudianteService, useValue: servicio },
        { provide: UniversidadService, useValue: { listar: () => of([{ idUniversidad: 1, nombre: 'Uni Uno' }]) } },
        { provide: ActivatedRoute, useValue: { queryParamMap: params$ } },
        { provide: Router, useValue: { navigate: (r: unknown, e: unknown) => { navegado = [r, e]; } } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  it('debe pedir el ranking general (top 20) y mostrarlo en orden', () => {
    const f = TestBed.createComponent(RankingComponent);
    f.detectChanges();
    expect(pedidos).toEqual([undefined]);
    expect(f.componentInstance.limite).toBe(20);
    const filas = f.nativeElement.querySelectorAll('.fila');
    expect(filas.length).toBe(2);
    expect(filas[0].textContent).toContain('Marta X');
    expect(filas[0].textContent).toContain('9');
    expect(filas[0].querySelector('.pos').textContent.trim()).toBe('1');
  });

  it('debe pedir el ranking de la universidad indicada en la URL', () => {
    params$.next(convertToParamMap({ universidad: '1' }));
    const f = TestBed.createComponent(RankingComponent);
    f.detectChanges();
    expect(pedidos).toEqual([1]);
    expect(f.componentInstance.nombreUniversidad()).toBe('Uni Uno');
  });

  it('debe recargar al cambiar la universidad en la URL', () => {
    const f = TestBed.createComponent(RankingComponent);
    f.detectChanges();
    params$.next(convertToParamMap({ universidad: '1' }));
    expect(pedidos).toEqual([undefined, 1]);
  });

  it('al elegir una universidad navega con ?universidad=ID, y vuelve al general con vacío', () => {
    const comp = TestBed.createComponent(RankingComponent).componentInstance;
    comp.elegir('1');
    expect(navegado[1]).toEqual({ queryParams: { universidad: '1' } });
    comp.elegir('');
    expect(navegado[1]).toEqual({ queryParams: {} });
  });

  it('debe mostrar un mensaje si el ranking está vacío', () => {
    servicio.ranking = () => of([]);
    const f = TestBed.createComponent(RankingComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('Todavía nadie tiene me gusta');
  });

  it('debe mostrar un mensaje si el servidor falla', () => {
    servicio.ranking = () => throwError(() => new Error('falló'));
    const f = TestBed.createComponent(RankingComponent);
    f.detectChanges();
    expect(f.nativeElement.textContent).toContain('No pudimos cargar el ranking');
  });
});
