import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { AdminUsuariosComponent } from './admin-usuarios.component';
import { AdminReportesComponent } from './admin-reportes.component';
import { AdminComentariosComponent } from './admin-comentarios.component';
import { AdminMeGustaComponent } from './admin-megusta.component';
import { AdminCatalogosComponent } from './admin-catalogos.component';
import { AdminService } from '../services/admin.service';
import { CatalogoService } from '../services/catalogo.service';
import { UniversidadService } from '../services/universidad.service';

const usuario = (id: number, nombre: string, rol = 'ESTUDIANTE'): any => ({
  id, nombre, apellido: 'X', nombreCompleto: nombre + ' X', correo: nombre.toLowerCase() + '@x.co', rol, estudios: [], redes: [], intereses: []
});
const reporte = (id: number, estado = 'PENDIENTE'): any => ({
  idReporte: id, idRealizador: 1, nombreRealizador: 'Ana', idReportado: 2, nombreReportado: 'Luis', tipoReporte: 'SPAM', estado, descripcion: 'texto', fechaHora: '2026-10-01T10:00:00'
});

async function montar(componente: any, mock: any, extra: any[] = []): Promise<ComponentFixture<any>> {
  await TestBed.configureTestingModule({
    declarations: [componente], imports: [FormsModule],
    providers: [{ provide: AdminService, useValue: mock }, ...extra], schemas: [NO_ERRORS_SCHEMA]
  }).compileComponents();
  const f = TestBed.createComponent(componente);
  f.detectChanges();
  return f;
}

describe('Admin · usuarios', () => {
  const a: any = {}; let borrados: number[] = []; let guardado: any = null;
  beforeEach(() => {
    borrados = []; guardado = null;
    a.listarUsuarios = () => of([usuario(1, 'Ana'), usuario(2, 'Luis'), usuario(3, 'Admin', 'ADMIN')]);
    a.obtenerUsuario = (id: number) => of(usuario(id, 'Fresco'));
    a.crearUsuario = (d: unknown) => { guardado = ['crear', d]; return of(usuario(9, 'Nuevo')); };
    a.actualizarUsuario = (id: number, d: unknown) => { guardado = ['actualizar', id, d]; return of(usuario(id, 'X')); };
    a.eliminarUsuario = (id: number) => { borrados.push(id); return of(undefined); };
  });

  it('lista los usuarios y filtra por texto', async () => {
    const f = await montar(AdminUsuariosComponent, a);
    expect(f.nativeElement.querySelectorAll('tbody tr').length).toBe(3);
    f.componentInstance.filtro.set('luis');
    f.detectChanges();
    expect(f.nativeElement.querySelectorAll('tbody tr').length).toBe(1);
  });

  it('crea un usuario con POST', async () => {
    const f = await montar(AdminUsuariosComponent, a);
    const c = f.componentInstance as any;
    c.nuevo();
    c.guardar({ correo: 'n@x.co' } as any);
    expect(guardado).toEqual(['crear', { correo: 'n@x.co' }]);
    expect(c.formulario()).toBeNull();
    expect(c.exito()).toBe('Usuario creado.');
  });

  it('edita un usuario trayendo primero el dato fresco (GET /admin/usuarios/{id})', async () => {
    const f = await montar(AdminUsuariosComponent, a);
    const c = f.componentInstance as any;
    c.editar(usuario(2, 'Luis'));
    expect((c.formulario() as any).nombre).toBe('Fresco');
    c.guardar({ correo: 'l@x.co' } as any);
    expect(guardado[0]).toBe('actualizar');
    expect(guardado[1]).toBe(2);
  });

  it('elimina solo tras confirmar', async () => {
    const f = await montar(AdminUsuariosComponent, a);
    const c = f.componentInstance as any;
    c.pedirBorrado(usuario(2, 'Luis'));
    expect(borrados).toEqual([]);
    c.eliminar(usuario(2, 'Luis'));
    expect(borrados).toEqual([2]);
  });

  it('muestra el 409 del back (último administrador)', async () => {
    a.eliminarUsuario = () => throwError(() => new HttpErrorResponse({ status: 409, error: { message: 'No se puede eliminar al ultimo administrador' } }));
    const f = await montar(AdminUsuariosComponent, a);
    f.componentInstance.eliminar(usuario(3, 'Admin', 'ADMIN'));
    expect(f.componentInstance.error()).toContain('ultimo administrador');
  });
});

describe('Admin · reportes', () => {
  const a: any = {}; let filtroPedido: unknown = 'sin llamar'; let estados: string[] = []; let borrados: number[] = [];
  beforeEach(() => {
    estados = []; borrados = []; filtroPedido = 'sin llamar';
    a.listarReportes = (e?: string) => { filtroPedido = e; return of([reporte(1), reporte(2, 'RESUELTO')]); };
    a.obtenerReporte = (id: number) => of(reporte(id));
    a.actualizarReporte = (id: number, e: string) => { estados.push(`${id}:${e}`); return of(reporte(id, e)); };
    a.eliminarReporte = (id: number) => { borrados.push(id); return of(undefined); };
  });

  it('lista todos y filtra por estado (?estado=)', async () => {
    const f = await montar(AdminReportesComponent, a);
    expect(filtroPedido).toBeUndefined();
    expect(f.nativeElement.querySelectorAll('tbody tr').length).toBe(2);
    f.componentInstance.filtrar('PENDIENTE');
    expect(filtroPedido).toBe('PENDIENTE');
  });

  it('cambia el estado con PUT', async () => {
    const f = await montar(AdminReportesComponent, a);
    f.componentInstance.cambiarEstado(reporte(1), 'EN_REVISION');
    expect(estados).toEqual(['1:EN_REVISION']);
    expect(f.componentInstance.reportes().find((r: any) => r.idReporte === 1)?.estado).toBe('EN_REVISION');
  });

  it('abre el detalle con GET /admin/reportes/{id}', async () => {
    const f = await montar(AdminReportesComponent, a);
    f.componentInstance.ver(reporte(2));
    f.detectChanges();
    expect(f.componentInstance.detalle()?.idReporte).toBe(2);
    expect(f.nativeElement.textContent).toContain('Reporte #2');
  });

  it('elimina un reporte', async () => {
    const f = await montar(AdminReportesComponent, a);
    f.componentInstance.eliminar(reporte(1));
    expect(borrados).toEqual([1]);
  });
});

describe('Admin · comentarios y me gusta', () => {
  it('lista y elimina comentarios por autor + número', async () => {
    let borrado = '';
    const a: any = {
      listarComentarios: () => of([{ numeroComentario: 3, idAutor: 7, nombreAutor: 'A', idDestinatario: 8, nombreDestinatario: 'B', contenido: 'hola', fechaHora: '2026-10-01T10:00:00' }]),
      eliminarComentario: (u: number, n: number) => { borrado = `${u}/${n}`; return of(undefined); }
    };
    const f = await montar(AdminComentariosComponent, a);
    expect(f.nativeElement.textContent).toContain('hola');
    f.componentInstance.eliminar(f.componentInstance.comentarios()[0]);
    expect(borrado).toBe('7/3');
  });

  it('lista y elimina me gusta por autor + número', async () => {
    let borrado = '';
    const a: any = {
      listarMeGusta: () => of([{ numeroReaccion: 2, idAutor: 7, nombreAutor: 'A', idDestinatario: 8, nombreDestinatario: 'B', tipoReaccion: 'ME_GUSTA', fechaHora: '2026-10-01T10:00:00' }]),
      eliminarMeGusta: (u: number, n: number) => { borrado = `${u}/${n}`; return of(undefined); }
    };
    const f = await montar(AdminMeGustaComponent, a);
    expect(f.nativeElement.textContent).toContain('ME_GUSTA');
    f.componentInstance.eliminar(f.componentInstance.reacciones()[0]);
    expect(borrado).toBe('7/2');
  });
});

describe('Admin · catálogos', () => {
  const a: any = {}; const llamadas: string[] = [];
  const cat: any = {}; const uni: any = {};
  beforeEach(() => {
    llamadas.length = 0;
    cat.ciudades = () => of([{ idCiudad: 1, nombre: 'Bogota', departamento: 'Cundinamarca' }]);
    cat.carreras = () => of([{ idCarrera: 5, nombre: 'Medicina' }]);
    cat.redesSociales = () => of([{ idRedSocial: 3, nombreRedSocial: 'X', url: 'https://x.com' }]);
    cat.oferta = () => of([{ idCarrera: 5, carrera: 'Medicina', idUniversidad: 2, universidad: 'Uni', facultad: 'Salud' }]);
    cat.ciudad = (id: number) => of({ idCiudad: id, nombre: 'Fresca', departamento: 'D' });
    cat.carrera = (id: number) => of({ idCarrera: id, nombre: 'Fresca' });
    cat.redSocial = (id: number) => of({ idRedSocial: id, nombreRedSocial: 'Fresca', url: 'https://f.co' });
    uni.listar = (n?: string) => { llamadas.push('listar ' + (n ?? '')); return of([{ idUniversidad: 2, nombre: 'Uni', sigla: 'U', calle: null, barrio: null, numeroDireccion: null, ciudad: { idCiudad: 1, nombre: 'Bogota', departamento: 'C' } }]); };
    uni.obtener = (id: number) => of({ idUniversidad: id, nombre: 'Fresca', sigla: 'F', calle: 'c', barrio: 'b', numeroDireccion: '1', ciudad: { idCiudad: 1, nombre: 'Bogota', departamento: 'C' } });
    for (const m of ['crearCiudad', 'actualizarCiudad', 'eliminarCiudad', 'crearUniversidad', 'actualizarUniversidad', 'eliminarUniversidad', 'crearCarrera', 'actualizarCarrera',
      'eliminarCarrera', 'crearRedSocial', 'actualizarRedSocial', 'eliminarRedSocial', 'crearOferta', 'actualizarOferta', 'eliminarOferta']) {
      a[m] = (...args: unknown[]) => { llamadas.push(m + ' ' + JSON.stringify(args)); return of({}); };
    }
  });
  const crear = () => montar(AdminCatalogosComponent, a, [{ provide: CatalogoService, useValue: cat }, { provide: UniversidadService, useValue: uni }]);

  it('carga todos los catálogos', async () => {
    const f = await crear();
    const c = f.componentInstance as any;
    expect([c.ciudades().length, c.universidades().length, c.carreras().length, c.redes().length, c.oferta().length]).toEqual([1, 1, 1, 1, 1]);
  });

  it('ciudades: crea, edita (cargando por id) y elimina', async () => {
    const c = (await crear()).componentInstance as AdminCatalogosComponent;
    c.fCiudad = { id: null, nombre: ' Cali ', departamento: 'Valle' };
    c.guardarCiudad();
    c.editarCiudad({ idCiudad: 1, nombre: 'Bogota', departamento: 'C' });
    expect(c.fCiudad.nombre).toBe('Fresca');
    c.guardarCiudad();
    c.eliminarCiudad({ idCiudad: 1, nombre: 'Bogota', departamento: 'C' });
    expect(llamadas.filter(l => !l.startsWith('listar'))).toEqual([
      'crearCiudad [{"nombre":"Cali","departamento":"Valle"}]', 'actualizarCiudad [1,{"nombre":"Fresca","departamento":"D"}]', 'eliminarCiudad [1]']);
  });

  it('universidades: busca por nombre, crea, edita y elimina', async () => {
    const c = (await crear()).componentInstance as AdminCatalogosComponent;
    c.busquedaUni = 'bosque';
    c.cargar();
    expect(llamadas).toContain('listar bosque');
    c.fUni = { id: null, nombre: 'Nueva', sigla: 'N', calle: '', barrio: '', numeroDireccion: '', idCiudad: 1 };
    c.guardarUniversidad();
    c.editarUniversidad(c.universidades()[0]);
    expect(c.fUni.id).toBe(2);
    c.guardarUniversidad();
    c.eliminarUniversidad(c.universidades()[0]);
    const m = llamadas.filter(l => /Universidad/.test(l));
    expect(m[0]).toBe('crearUniversidad [{"nombre":"Nueva","sigla":"N","idCiudad":1}]');
    expect(m[1]).toContain('actualizarUniversidad [2,');
    expect(m[2]).toBe('eliminarUniversidad [2]');
  });

  it('carreras y redes sociales: crean, editan y eliminan', async () => {
    const c = (await crear()).componentInstance as AdminCatalogosComponent;
    c.fCarrera = { id: null, nombre: 'Derecho' }; c.guardarCarrera();
    c.editarCarrera({ idCarrera: 5, nombre: 'Medicina' }); c.guardarCarrera();
    c.eliminarCarrera({ idCarrera: 5, nombre: 'Medicina' });
    c.fRed = { id: null, nombreRedSocial: 'Threads', url: 'https://threads.net' }; c.guardarRed();
    c.editarRed({ idRedSocial: 3, nombreRedSocial: 'X', url: 'https://x.com' }); c.guardarRed();
    c.eliminarRed({ idRedSocial: 3, nombreRedSocial: 'X', url: 'https://x.com' });
    const m = llamadas.filter(l => !l.startsWith('listar'));
    expect(m.map(x => x.split(' ')[0])).toEqual(['crearCarrera', 'actualizarCarrera', 'eliminarCarrera', 'crearRedSocial', 'actualizarRedSocial', 'eliminarRedSocial']);
  });

  it('oferta: crea, corrige la facultad y elimina por (carrera, universidad)', async () => {
    const c = (await crear()).componentInstance as AdminCatalogosComponent;
    c.fOferta = { editando: false, idCarrera: 5, idUniversidad: 2, facultad: 'Salud' }; c.guardarOferta();
    c.editarOferta(c.oferta()[0]);
    c.fOferta.facultad = 'Ciencias de la Salud'; c.guardarOferta();
    c.eliminarOferta(c.oferta()[0]);
    const m = llamadas.filter(l => !l.startsWith('listar'));
    expect(m).toEqual(['crearOferta [{"idCarrera":5,"idUniversidad":2,"facultad":"Salud"}]', 'actualizarOferta [5,2,"Ciencias de la Salud"]', 'eliminarOferta [5,2]']);
  });

  it('valida campos vacíos sin llamar al back', async () => {
    const c = (await crear()).componentInstance as AdminCatalogosComponent;
    c.guardarCiudad(); c.guardarUniversidad(); c.guardarCarrera(); c.guardarRed(); c.guardarOferta();
    expect(llamadas.filter(l => !l.startsWith('listar'))).toEqual([]);
    expect(c.error()).not.toBe('');
  });

  it('muestra el 409 del back al eliminar algo en uso', async () => {
    a.eliminarCiudad = () => throwError(() => new HttpErrorResponse({ status: 409, error: { message: 'La ciudad todavia tiene universidades' } }));
    const c = (await crear()).componentInstance as AdminCatalogosComponent;
    c.eliminarCiudad({ idCiudad: 1, nombre: 'Bogota', departamento: 'C' });
    expect(c.error()).toContain('todavia tiene universidades');
  });
});
