import { MovimientoStock } from '../../domain/models/movimiento-stock.model';
import { MovimientoStockResponseDto } from '../dtos/movimiento-stock.dto';

export class MovimientoStockMapper {
  public static toDomain(dto: MovimientoStockResponseDto): MovimientoStock {
    return new MovimientoStock(
      dto.id,
      dto.producto,
      dto.sku,
      dto.cantidadAnterior,
      dto.cantidadNueva,
      dto.tipoMovimiento,
      dto.codigoReferencia,
      dto.usuario,
      dto.fechaMovimiento,
      dto.observacion
    );
  }

  public static toDomainList(dtos: MovimientoStockResponseDto[]): MovimientoStock[] {
    return dtos.map((dto) => this.toDomain(dto));
  }
}
