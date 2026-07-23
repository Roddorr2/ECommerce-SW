import { Routes } from '@angular/router';
import { ProductoComponent } from './presentation/pages/producto/producto.component';
import { CategoriaComponent } from './presentation/pages/categoria/categoria.component';
import { MovimientoStockComponent } from './presentation/pages/movimiento-stock/movimiento-stock.component';

export const CATALOG_ROUTES: Routes = [
  { path: 'productos', component: ProductoComponent, title: 'Productos' },
  { path: 'categorias', component: CategoriaComponent, title: 'Categorías' },
  { path: 'movimientos/:productoId', component: MovimientoStockComponent, title: 'Movimientos de Stock' },
  { path: '', redirectTo: 'productos', pathMatch: 'full'},
];
