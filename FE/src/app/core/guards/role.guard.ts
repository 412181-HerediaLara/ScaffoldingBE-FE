import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard = (expectedRole: 'USER' | 'ADMIN') => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.role() === expectedRole) {
    return true;
  }

  return router.parseUrl('/dashboard');
};
