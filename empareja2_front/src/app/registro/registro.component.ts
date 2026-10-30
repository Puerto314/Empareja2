import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { RegistroRequestModel } from '../models/registro-request.model';
import { PerfilFormValor } from '../perfil-form/perfil-form.component';
import { AuthService } from '../services/auth.service';
import { mensajeDeError } from '../utils/error.util';

// Pantalla para crear una cuenta nueva (POST /api/auth/registro).
@Component({
  standalone: false,
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  error = signal('');
  cargando = signal(false);

  registrar(valor: PerfilFormValor): void {
    this.error.set('');
    this.cargando.set(true);
    this.authService.registrar(valor as RegistroRequestModel).subscribe({
      next: respuesta => {
        // El backend responde 201 con token: la persona queda con la sesión iniciada.
        this.authService.guardarSesion(respuesta);
        this.cargando.set(false);
        this.router.navigate(['/universidades']);
      },
      error: (err: unknown) => {
        this.error.set(mensajeDeError(err, 'No pudimos crear tu cuenta. Intenta de nuevo.'));
        this.cargando.set(false);
      }
    });
  }
}
