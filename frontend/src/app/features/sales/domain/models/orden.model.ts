import { EstadoOrden } from './estado-orden.enum';

export interface ClienteBasicoResponse {
  id: number;
  nombre: string;
}

export interface MetodoPagoResponse {
  id: number;
  nombre: string;
}

export interface OrdenDetalleResponse {
  id: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  producto: {
    id: number;
    nombre: string;
    sku: string;
  };
}

export class Orden {
  constructor(
    public readonly id: number,
    public readonly fechaOrden: string,
    public readonly direccionEnvio: string | null,
    public readonly total: number,
    public readonly estado: { codigo: EstadoOrden },
    public readonly cliente: ClienteBasicoResponse,
    public readonly metodoPago: MetodoPagoResponse,
    public readonly detalles: OrdenDetalleResponse[]
  ) {}
}

export class OrdenResumen {
  constructor(
    public readonly id: number,
    public readonly fechaOrden: string,
    public readonly estado: EstadoOrden,
    public readonly total: number,
    public readonly cantidadItems: number,
    public readonly cliente: ClienteBasicoResponse
  ) {}
}
