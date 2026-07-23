import { Routes } from '@angular/router';
import { DashboardAdminComponent } from './presentation/pages/dashboard-admin/dashboard-admin.component';
import { PerfilAdminComponent } from './presentation/pages/perfil-admin/perfil-admin.component';
import { UsuarioComponent } from './presentation/pages/usuario/usuario.component';
import { RolComponent } from './presentation/pages/rol/rol.component';
import { AreaComponent } from './presentation/pages/area/area.component';
import { CargoComponent } from './presentation/pages/cargo/cargo.component';
import { EmpleadoComponent } from './presentation/pages/empleado/empleado.component';
import { ClienteComponent } from './presentation/pages/cliente/cliente.component';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: DashboardAdminComponent,
    children: [
      { path: 'dashboard', redirectTo: '/admin/perfil', pathMatch: 'full' },
      { path: 'perfil', component: PerfilAdminComponent, title: 'Datos Personales' },
      {
        path: 'catalog',
        loadChildren: () => import('../catalog/catalog.routes').then(m => m.CATALOG_ROUTES),
      },
      {
        path: 'purchases',
        loadChildren: () => import('../purchases/purchases.routes').then(m => m.PURCHASES_ROUTES),
      },
      {
        path: 'sales',
        loadChildren: () => import('../sales/sales.routes').then(m => m.SALES_ROUTES),
      },
      { path: 'areas',     component: AreaComponent,     title: 'Áreas' },
      { path: 'cargos',    component: CargoComponent,    title: 'Cargos' },
      { path: 'empleados', component: EmpleadoComponent, title: 'Empleados' },
      { path: 'roles',     component: RolComponent,      title: 'Roles' },
      { path: 'usuarios',  component: UsuarioComponent,  title: 'Usuarios' },
      { path: 'clientes',  component: ClienteComponent, title: 'Clientes' },
      { path: '', redirectTo: '/admin/perfil', pathMatch: 'full' },
    ],
  },
];
