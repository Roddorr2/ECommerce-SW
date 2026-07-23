import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AUTH_REPOSITORY } from '../../features/auth/domain/repositories/auth.repository';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AUTH_REPOSITORY);
  const token = auth.obtenerToken();

  if (!token) return next(req);

  return next(
    req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    }),
  );
};
