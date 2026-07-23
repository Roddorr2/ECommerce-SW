export interface MovimientoStockResponseDto {
  id: number;
  producto: string;
  sku: string;
  cantidadAnterior: number;
  cantidadNueva: number;
  diferencia: number;
  tipoMovimiento: string;
  codigoReferencia: string;
  usuario: string;
  fechaMovimiento: string;
  observacion: string;
}
