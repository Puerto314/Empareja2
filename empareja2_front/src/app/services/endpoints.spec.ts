import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Observable } from 'rxjs';

import { API_URL } from '../config/api.config';
import { AdminService } from './admin.service';
import { AuthService } from './auth.service';
import { CatalogoService } from './catalogo.service';
import { CuentaService } from './cuenta.service';
import { InteraccionService } from './interaccion.service';
import { UniversidadService } from './universidad.service';

// Verifica que cada método del front llame al método HTTP y a la ruta exacta del back.
describe('Endpoints del backend', () => {
  let http: HttpTestingController;
  let admin: AdminService;
  let catalogo: CatalogoService;
  let cuenta: CuentaService;
  let interaccion: InteraccionService;
  let auth: AuthService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    http = TestBed.inject(HttpTestingController);
    admin = TestBed.inject(AdminService);
    catalogo = TestBed.inject(CatalogoService);
    cuenta = TestBed.inject(CuentaService);
    interaccion = TestBed.inject(InteraccionService);
    auth = TestBed.inject(AuthService);
  });
  afterEach(() => http.verify());

  const casos: { nombre: string; metodo: string; ruta: string; llamar: () => Observable<unknown> }[] = [];
  const caso = (metodo: string, ruta: string, llamar: () => Observable<unknown>) =>
    casos.push({ nombre: `${metodo} ${ruta}`, metodo, ruta, llamar });

  // La lambda se evalúa dentro del test (los servicios se inyectan en beforeEach).
  caso('GET', '/usuarios/me', () => cuenta.miPerfil());
  caso('PUT', '/usuarios/me', () => cuenta.actualizar({} as any));
  caso('DELETE', '/usuarios/me', () => cuenta.eliminar());
  caso('POST', '/usuarios/5/me-gusta', () => interaccion.darMeGusta(5));
  caso('DELETE', '/usuarios/5/me-gusta', () => interaccion.quitarMeGusta(5));
  caso('GET', '/usuarios/5/me-gusta', () => interaccion.listarMeGusta(5));
  caso('POST', '/usuarios/5/comentarios', () => interaccion.enviarComentario(5, 'hola'));
  caso('GET', '/usuarios/me/comentarios/recibidos', () => interaccion.comentariosRecibidos());
  caso('GET', '/usuarios/me/comentarios/enviados', () => interaccion.comentariosEnviados());
  caso('POST', '/usuarios/5/reportes', () => interaccion.reportar(5, { tipoReporte: 'SPAM' as any }));
  caso('GET', '/catalogos/ciudades', () => catalogo.ciudades());
  caso('GET', '/catalogos/ciudades/2', () => catalogo.ciudad(2));
  caso('GET', '/catalogos/carreras', () => catalogo.carreras());
  caso('GET', '/catalogos/carreras/2', () => catalogo.carrera(2));
  caso('GET', '/catalogos/redes-sociales', () => catalogo.redesSociales());
  caso('GET', '/catalogos/redes-sociales/2', () => catalogo.redSocial(2));
  caso('GET', '/catalogos/oferta', () => catalogo.oferta());
  caso('POST', '/auth/registro', () => auth.registrar({} as any));
  // ---- Administración
  caso('GET', '/admin/usuarios', () => admin.listarUsuarios());
  caso('GET', '/admin/usuarios/3', () => admin.obtenerUsuario(3));
  caso('POST', '/admin/usuarios', () => admin.crearUsuario({} as any));
  caso('PUT', '/admin/usuarios/3', () => admin.actualizarUsuario(3, {} as any));
  caso('DELETE', '/admin/usuarios/3', () => admin.eliminarUsuario(3));
  caso('GET', '/admin/reportes', () => admin.listarReportes());
  caso('GET', '/admin/reportes/4', () => admin.obtenerReporte(4));
  caso('PUT', '/admin/reportes/4', () => admin.actualizarReporte(4, 'RESUELTO' as any));
  caso('DELETE', '/admin/reportes/4', () => admin.eliminarReporte(4));
  caso('GET', '/admin/comentarios', () => admin.listarComentarios());
  caso('DELETE', '/admin/comentarios/7/2', () => admin.eliminarComentario(7, 2));
  caso('GET', '/admin/me-gusta', () => admin.listarMeGusta());
  caso('DELETE', '/admin/me-gusta/7/2', () => admin.eliminarMeGusta(7, 2));
  caso('POST', '/admin/catalogos/ciudades', () => admin.crearCiudad({} as any));
  caso('PUT', '/admin/catalogos/ciudades/1', () => admin.actualizarCiudad(1, {} as any));
  caso('DELETE', '/admin/catalogos/ciudades/1', () => admin.eliminarCiudad(1));
  caso('POST', '/admin/catalogos/universidades', () => admin.crearUniversidad({} as any));
  caso('PUT', '/admin/catalogos/universidades/1', () => admin.actualizarUniversidad(1, {} as any));
  caso('DELETE', '/admin/catalogos/universidades/1', () => admin.eliminarUniversidad(1));
  caso('POST', '/admin/catalogos/carreras', () => admin.crearCarrera({} as any));
  caso('PUT', '/admin/catalogos/carreras/1', () => admin.actualizarCarrera(1, {} as any));
  caso('DELETE', '/admin/catalogos/carreras/1', () => admin.eliminarCarrera(1));
  caso('POST', '/admin/catalogos/redes-sociales', () => admin.crearRedSocial({} as any));
  caso('PUT', '/admin/catalogos/redes-sociales/1', () => admin.actualizarRedSocial(1, {} as any));
  caso('DELETE', '/admin/catalogos/redes-sociales/1', () => admin.eliminarRedSocial(1));
  caso('POST', '/admin/catalogos/oferta', () => admin.crearOferta({} as any));
  caso('PUT', '/admin/catalogos/oferta/2/3', () => admin.actualizarOferta(2, 3, 'Facultad X'));
  caso('DELETE', '/admin/catalogos/oferta/2/3', () => admin.eliminarOferta(2, 3));

  for (const c of casos) {
    it(`${c.nombre}`, () => {
      c.llamar().subscribe();
      const req = http.expectOne(r => r.url === `${API_URL}${c.ruta}`);
      expect(req.request.method).toBe(c.metodo);
      req.flush(null);
    });
  }

  it('GET /admin/reportes?estado=PENDIENTE filtra por estado', () => {
    admin.listarReportes('PENDIENTE' as any).subscribe();
    const req = http.expectOne(r => r.url === `${API_URL}/admin/reportes`);
    expect(req.request.params.get('estado')).toBe('PENDIENTE');
    req.flush([]);
  });

  it('GET /catalogos/oferta?universidadId=3 filtra por universidad', () => {
    catalogo.oferta(3).subscribe();
    const req = http.expectOne(r => r.url === `${API_URL}/catalogos/oferta`);
    expect(req.request.params.get('universidadId')).toBe('3');
    req.flush([]);
  });

  it('GET /catalogos/universidades?nombre= busca por nombre', () => {
    // UniversidadService.listar(nombre) se cubre aquí por ser parte del contrato público
    const svc = TestBed.inject(UniversidadService);
    svc.listar('bosque').subscribe();
    const req = http.expectOne(r => r.url === `${API_URL}/catalogos/universidades`);
    expect(req.request.params.get('nombre')).toBe('bosque');
    req.flush([]);
  });

  it('PUT /admin/reportes/{id} envía { estado }', () => {
    admin.actualizarReporte(4, 'EN_REVISION' as any).subscribe();
    const req = http.expectOne(`${API_URL}/admin/reportes/4`);
    expect(req.request.body).toEqual({ estado: 'EN_REVISION' });
    req.flush({});
  });

  it('PUT /admin/catalogos/oferta/{c}/{u} envía { facultad }', () => {
    admin.actualizarOferta(2, 3, 'Ingeniería').subscribe();
    const req = http.expectOne(`${API_URL}/admin/catalogos/oferta/2/3`);
    expect(req.request.body).toEqual({ facultad: 'Ingeniería' });
    req.flush({});
  });

  it('POST /usuarios/{id}/comentarios envía { contenido }', () => {
    interaccion.enviarComentario(5, 'Hola').subscribe();
    const req = http.expectOne(`${API_URL}/usuarios/5/comentarios`);
    expect(req.request.body).toEqual({ contenido: 'Hola' });
    req.flush({});
  });

  it('GET /usuarios/me guarda el usuario al día en la sesión', () => {
    cuenta.miPerfil().subscribe();
    http.expectOne(`${API_URL}/usuarios/me`).flush({ id: 9, rol: 'ESTUDIANTE', nombre: 'Ana' });
    expect(auth.obtenerUsuario()?.id).toBe(9);
  });

  it('DELETE /usuarios/me cierra la sesión local', () => {
    auth.guardarSesion({ token: 't', tokenType: 'Bearer', expiresInMs: 1, usuario: { id: 1 } as any });
    cuenta.eliminar().subscribe();
    http.expectOne(`${API_URL}/usuarios/me`).flush(null, { status: 204, statusText: 'No Content' });
    expect(auth.estaLogueado()).toBe(false);
  });
});

