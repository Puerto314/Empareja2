import { Component } from '@angular/core';

// Contenedor del panel de administración: pestañas + las pantallas hijas.
@Component({
  standalone: false,
  selector: 'app-admin',
  template: `
    <main class="page-wrap">
      <h1>Administración</h1>
      <p class="muted">Gestiona usuarios, moderación y datos de referencia.</p>
      <nav class="tabs" aria-label="Secciones de administración">
        <a routerLink="usuarios" routerLinkActive="active">Usuarios</a>
        <a routerLink="reportes" routerLinkActive="active">Reportes</a>
        <a routerLink="comentarios" routerLinkActive="active">Comentarios</a>
        <a routerLink="me-gusta" routerLinkActive="active">Me gusta</a>
        <a routerLink="catalogos" routerLinkActive="active">Catálogos</a>
      </nav>
      <router-outlet />
    </main>`
})
export class AdminComponent {}
