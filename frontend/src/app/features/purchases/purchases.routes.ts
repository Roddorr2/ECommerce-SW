import { Routes } from '@angular/router';
import { TipoProveedorComponent } from './presentation/pages/tipo-proveedor/tipo-proveedor.component';
import { ProveedorComponent } from './presentation/pages/proveedor/proveedor.component';
import { CompraComponent } from './presentation/pages/compra/compra.component';

export const PURCHASES_ROUTES: Routes = [
  { path: 'compras', component: CompraComponent },
  { path: 'proveedores', component: ProveedorComponent },
  { path: 'tipos-proveedor', component: TipoProveedorComponent },
  { path: '', redirectTo: 'compras', pathMatch: 'full' },
];
