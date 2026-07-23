import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { MovimientoStock } from '../../domain/models/movimiento-stock.model';
import { MovimientoStockRepository } from '../../domain/repositories/movimiento-stock.repository';
import { MovimientoStockResponseDto } from '../dtos/movimiento-stock.dto';
import { MovimientoStockMapper } from '../mappers/movimiento-stock.mapper';

@Injectable({
  providedIn: 'root',
})
export class MovimientoStockHttpRepository implements MovimientoStockRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/movimientos-stock`;

  public obtenerMovimientosPorProducto(productoId: number): Observable<MovimientoStock[]> {
    return this.http.get<MovimientoStockResponseDto[]>(`${this.apiUrl}/producto/${productoId}`).pipe(
      map((dtos) => MovimientoStockMapper.toDomainList(dtos))
    );
  }
}
