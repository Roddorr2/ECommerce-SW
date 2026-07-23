import { Routes } from '@angular/router';
import { OrdenesComponent } from './presentation/pages/ordenes/ordenes.component';
import { MetodoPagoComponent } from './presentation/pages/metodo-pago/metodo-pago.component';
import { CarritoComponent } from './presentation/pages/carrito/carrito.component';

export const SALES_ROUTES: Routes = [
  { path: 'ordenes', component: OrdenesComponent, title: 'Órdenes' },
  { path: 'metodos-pago', component: MetodoPagoComponent, title: 'Métodos de Pago' },
  { path: 'carrito', component: CarritoComponent, title: 'CarritoComponent' },
  { path: '', redirectTo: 'ordenes', pathMatch: 'full' },
];
