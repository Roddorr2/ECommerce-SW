import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { clientGuard } from './core/guards/client.guard';
import { HomeComponent } from './features/public/pages/home/home.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent, title: 'Inicio' },
  {
    path: 'faq',
    loadComponent: () => import('./features/public/pages/faq/faq.component').then((m) => m.FaqComponent),
    title: 'Preguntas Frecuentes',
  },
  {
    path: 'politicas-envio',
    loadComponent: () => import('./features/public/pages/politicas-envio/politicas-envio.component').then((m) => m.PoliticasEnvioComponent),
    title: 'Políticas de Envío',
  },
  {
    path: 'metodos-pago',
    loadComponent: () => import('./features/public/pages/metodos-pago/metodos-pago.component').then((m) => m.MetodosPagoComponent),
    title: 'Métodos de Pago',
  },
  {
    path: 'soporte-tecnico',
    loadComponent: () => import('./features/public/pages/soporte-tecnico/soporte-tecnico.component').then((m) => m.SoporteTecnicoComponent),
    title: 'Soporte Técnico',
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'admin',
    canActivate: [authGuard, adminGuard],
    loadChildren: () => import('./features/admin/admin.routes').then((m) => m.ADMIN_ROUTES),
  },
  {
    path: 'cliente',
    canActivate: [authGuard, clientGuard],
    loadChildren: () => import('./features/client/client.routes').then((m) => m.CLIENTE_ROUTES),
  },
  { path: '**', redirectTo: 'auth/login' },
];

