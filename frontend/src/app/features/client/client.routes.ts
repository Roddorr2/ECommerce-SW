import { Routes } from '@angular/router';
import { DashboardClientComponent } from './pages/dashboard-client/dashboard-client.component';
import { ProductosClienteComponent } from './pages/productos/productos.component';
import { OrdenesClienteComponent } from './pages/ordenes/ordenes.component';
import { CarritoComponent } from '../sales/presentation/pages/carrito/carrito.component';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { CambiarContrasenaComponent } from './pages/cambiar-contrasena/cambiar-contrasena.component';
import { DireccionesComponent } from './pages/direcciones/direcciones.component';

export const CLIENTE_ROUTES: Routes = [
  {
    path: '',
    component: DashboardClientComponent,
    children: [
      { path: 'dashboard', redirectTo: '/cliente/perfil', pathMatch: 'full' },
      { path: 'perfil', component: PerfilComponent, title: 'Datos Personales' },
      { path: 'cambiar-contrasena', component: CambiarContrasenaComponent, title: 'Cambiar Contraseña' },
      { path: 'direcciones', component: DireccionesComponent, title: 'Direcciones' },
      {
        path: 'productos',
        component: ProductosClienteComponent,
        title: 'Productos',
      },
      {
        path: 'carrito',
        component: CarritoComponent,
        title: 'CarritoComponent',
      },
      {
        path: 'mis-ordenes',
        component: OrdenesClienteComponent,
        title: 'Mis Órdenes',
      },
      { path: '', redirectTo: '/cliente/perfil', pathMatch: 'full' },
    ],
  },
];
