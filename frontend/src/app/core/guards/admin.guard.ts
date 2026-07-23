import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AUTH_REPOSITORY } from '../../features/auth/domain/repositories/auth.repository';

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AUTH_REPOSITORY);
  const router = inject(Router);

  const rol = auth.obtenerRol()?.toUpperCase();

  return (rol === 'ADMINISTRADOR' || rol === 'EMPLEADO')
    || router.createUrlTree(['/auth/login']);
};
