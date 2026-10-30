import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

import { PerfilFormComponent, PerfilFormValor } from './perfil-form.component';
import { CatalogoService } from '../services/catalogo.service';
import { UniversidadService } from '../services/universidad.service';

const unis = [
  { idUniversidad: 1, nombre: 'Uni Uno', sigla: 'U1', calle: null, barrio: null, numeroDireccion: null, ciudad: null },
  { idUniversidad: 2, nombre: 'Uni Dos', sigla: 'U2', calle: null, barrio: null, numeroDireccion: null, ciudad: null }
];
const oferta = [
  { idCarrera: 10, carrera: 'Medicina', idUniversidad: 1, universidad: 'Uni Uno', facultad: 'F' },
  { idCarrera: 11, carrera: 'Derecho', idUniversidad: 2, universidad: 'Uni Dos', facultad: 'F' }
];

describe('PerfilFormComponent', () => {
  let fixture: ComponentFixture<PerfilFormComponent>;
  let comp: PerfilFormComponent;
  let emitido: PerfilFormValor | null = null;

  const crear = async (modo: 'registro' | 'edicion' | 'admin', inicial: any = null) => {
    await TestBed.configureTestingModule({
      declarations: [PerfilFormComponent],
      imports: [FormsModule],
      providers: [
        { provide: UniversidadService, useValue: { listar: () => of(unis) } },
        { provide: CatalogoService, useValue: { oferta: () => of(oferta), redesSociales: () => of([{ idRedSocial: 1, nombreRedSocial: 'Instagram', url: 'https://instagram.com' }]) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(PerfilFormComponent);
    comp = fixture.componentInstance;
    comp.modo = modo;
    comp.inicial = inicial;
    comp.enviar.subscribe(v => (emitido = v));
    fixture.componentRef.setInput('modo', modo);
    if (inicial) { fixture.componentRef.setInput('inicial', inicial); }
    fixture.detectChanges();
  };

  const llenarValido = () => {
    comp.correo = 'ana@unbosque.edu.co'; comp.contrasena = '12345678'; comp.documento = '1012345678';
    comp.nombre = 'Ana'; comp.apellido = 'Pérez'; comp.fechaNacimiento = '2000-05-10';
    comp.estudios = [{ idUniversidad: 1, idCarrera: 10, semestre: 4 }];
  };

  beforeEach(() => { emitido = null; });

  it('debe cargar universidades, oferta y redes al abrir', async () => {
    await crear('registro');
    expect(comp.universidades().length).toBe(2);
    expect(comp.oferta().length).toBe(2);
    expect(comp.redesDisponibles().length).toBe(1);
  });

  it('en registro empieza con una fila de estudio', async () => {
    await crear('registro');
    expect(comp.estudios.length).toBe(1);
  });

  it('solo ofrece las carreras de la universidad elegida', async () => {
    await crear('registro');
    expect(comp.carrerasDe(1).map(o => o.carrera)).toEqual(['Medicina']);
    expect(comp.carrerasDe(null)).toEqual([]);
  });

  it('al cambiar de universidad reinicia la carrera', async () => {
    await crear('registro');
    const fila = { idUniversidad: 2, idCarrera: 10, semestre: 3 };
    comp.cambiarUniversidad(fila);
    expect(fila.idCarrera).toBeNull();
  });

  it('en registro no ofrece TI como tipo de documento', async () => {
    await crear('registro');
    expect(comp.tiposDocumento).not.toContain('TI' as any);
  });

  it('rechaza correo mal formado, clave corta y documento inválido', async () => {
    await crear('registro');
    llenarValido();
    comp.correo = 'sin-arroba';
    expect(comp.validar()).toContain('correo');
    comp.correo = 'ana@unbosque.edu.co'; comp.contrasena = '123';
    expect(comp.validar()).toContain('contraseña');
    comp.contrasena = '12345678'; comp.documento = '12ab';
    expect(comp.validar()).toContain('documento');
  });

  it('rechaza menores de 18 años', async () => {
    await crear('registro');
    llenarValido();
    comp.fechaNacimiento = new Date(new Date().getFullYear() - 10, 0, 1).toISOString().slice(0, 10);
    expect(comp.validar()).toContain('al menos 18');
  });

  it('exige completar los estudios y no repetir carrera', async () => {
    await crear('registro');
    llenarValido();
    comp.estudios = [{ idUniversidad: 1, idCarrera: null, semestre: 3 }];
    expect(comp.validar()).toContain('Completa');
    comp.estudios = [{ idUniversidad: 1, idCarrera: 10, semestre: 3 }, { idUniversidad: 1, idCarrera: 10, semestre: 4 }];
    expect(comp.validar()).toContain('repitas');
    comp.estudios = [{ idUniversidad: 1, idCarrera: 10, semestre: 25 }];
    expect(comp.validar()).toContain('entre 1 y 20');
  });

  it('valida las redes sociales (enlace http/https y sin repetir)', async () => {
    await crear('registro');
    llenarValido();
    comp.redes = [{ idRedSocial: 1, urlPerfil: 'instagram.com/ana', alias: '' }];
    expect(comp.validar()).toContain('http');
    comp.redes = [{ idRedSocial: 1, urlPerfil: 'https://i.com/a', alias: '' }, { idRedSocial: 1, urlPerfil: 'https://i.com/b', alias: '' }];
    expect(comp.validar()).toContain('repitas');
  });

  it('emite el registro con la forma exacta de RegistroRequestDTO', async () => {
    await crear('registro');
    llenarValido();
    comp.genero = 'FEMENINO' as any;
    comp.interesesTexto = 'Cine, rock, cine, ';
    comp.redes = [{ idRedSocial: 1, urlPerfil: 'https://instagram.com/ana', alias: '@ana' }];
    comp.guardar();
    expect(emitido).toEqual({
      nombre: 'Ana', apellido: 'Pérez', genero: 'FEMENINO', fechaNacimiento: '2000-05-10', fotoPerfil: '',
      intereses: ['Cine', 'rock'], estudios: [{ idCarrera: 10, idUniversidad: 1, semestre: 4 }],
      redes: [{ idRedSocial: 1, urlPerfil: 'https://instagram.com/ana', alias: '@ana' }],
      correo: 'ana@unbosque.edu.co', contrasena: '12345678', tipoDocumento: 'CC', documento: '1012345678'
    } as any);
  });

  it('no emite nada si hay un error de validación', async () => {
    await crear('registro');
    comp.guardar();
    expect(emitido).toBeNull();
    expect(comp.errorLocal()).not.toBe('');
  });

  it('en edición no pide cuenta y emite sin correo ni documento', async () => {
    const perfil: any = {
      id: 1, correo: 'ana@x.co', rol: 'ESTUDIANTE', tipoDocumento: 'CC', documento: '123456', genero: null, nombre: 'Ana', apellido: 'Gil',
      fechaNacimiento: '2000-01-01', fotoPerfil: null, intereses: ['Cine'],
      estudios: [{ idCarrera: 10, carrera: 'Medicina', idUniversidad: 1, universidad: 'Uni Uno', siglaUniversidad: 'U1', semestre: 2 }], redes: []
    };
    await crear('edicion', perfil);
    expect(comp.pideCuenta).toBe(false);
    comp.guardar();
    expect(emitido).toBeTruthy();
    expect((emitido as any).correo).toBeUndefined();
    expect((emitido as any).documento).toBeUndefined();
    expect((emitido as any).intereses).toEqual(['Cine']);
    expect((emitido as any).estudios).toEqual([{ idCarrera: 10, idUniversidad: 1, semestre: 2 }]);
  });

  it('en admin permite editar sin contraseña y no la envía', async () => {
    const perfil: any = {
      id: 5, correo: 'luis@x.co', rol: 'ESTUDIANTE', tipoDocumento: 'CC', documento: '987654', genero: 'MASCULINO', nombre: 'Luis', apellido: 'Paz',
      fechaNacimiento: '1999-01-01', fotoPerfil: null, intereses: [],
      estudios: [{ idCarrera: 11, carrera: 'Derecho', idUniversidad: 2, universidad: 'Uni Dos', siglaUniversidad: 'U2', semestre: 5 }], redes: []
    };
    await crear('admin', perfil);
    comp.guardar();
    expect(emitido).toBeTruthy();
    expect((emitido as any).contrasena).toBeUndefined();
    expect((emitido as any).rol).toBe('ESTUDIANTE');
  });

  it('en admin una cuenta ADMIN no exige estudios ni fecha de nacimiento', async () => {
    await crear('admin');
    comp.correo = 'Puerto314'; comp.contrasena = 'claveSegura1'; comp.documento = '1'; comp.rol = 'ADMIN' as any;
    comp.nombre = 'Admin'; comp.apellido = 'Sistema'; comp.estudios = [];
    expect(comp.validar()).toBe('');
    comp.guardar();
    expect((emitido as any).rol).toBe('ADMIN');
    expect((emitido as any).estudios).toEqual([]);
  });
});
