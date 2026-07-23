import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AUTH_REPOSITORY } from '../../features/auth/domain/repositories/auth.repository';

export const clientGuard: CanActivateFn = () => {
  const auth = inject(AUTH_REPOSITORY);
  const router = inject(Router);

  return auth.obtenerRol()?.toUpperCase() === 'CLIENTE'
    || router.createUrlTree(['/auth/login']);
};
