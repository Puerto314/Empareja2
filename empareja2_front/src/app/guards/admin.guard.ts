import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// Solo deja pasar a administradores (el backend también lo exige con 403 en /api/admin/**).
export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  if (auth.estaLogueado() && auth.esAdmin()) {
    return true;
  }
  inject(Router).navigate([auth.estaLogueado() ? '/universidades' : '/login']);
  return false;
};
