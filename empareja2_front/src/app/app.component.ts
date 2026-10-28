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

  constructor() {
    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
<<<<<<< HEAD
      .subscribe(e => this.mostrarBarra = !(e as NavigationEnd).urlAfterRedirects.startsWith('/login'));
=======
      .subscribe((e: any) => {
        const url: string = e.urlAfterRedirects;
        this.mostrarBarra = !url.startsWith('/login') && !url.startsWith('/registro');
      });
  }

  get nombre(): string {
    return this.authService.obtenerUsuario()?.nombre ?? '';
>>>>>>> main
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
