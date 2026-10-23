import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  if (inject(AuthService).estaLogueado()) {
    return true;
  }
  inject(Router).navigate(['/login']);
  return false;
};
