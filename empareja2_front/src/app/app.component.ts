import { Component, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from './services/auth.service';

@Component({
  standalone: false,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private router = inject(Router);
  private authService = inject(AuthService);
  mostrarBarra = false;
  get esAdmin(): boolean { return this.authService.esAdmin(); }
  get nombreUsuario(): string { return this.authService.obtenerUsuario()?.nombre ?? ''; }

  constructor() {
    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe(e => this.mostrarBarra = !/^\/(login|registro)/.test((e as NavigationEnd).urlAfterRedirects));
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
