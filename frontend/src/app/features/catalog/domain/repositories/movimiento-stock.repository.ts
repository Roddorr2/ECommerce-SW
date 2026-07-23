import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { MovimientoStock } from '../models/movimiento-stock.model';

export interface MovimientoStockRepository {
  obtenerMovimientosPorProducto(productoId: number): Observable<MovimientoStock[]>;
}

export const MOVIMIENTO_STOCK_REPOSITORY = new InjectionToken<MovimientoStockRepository>('MovimientoStockRepository');
