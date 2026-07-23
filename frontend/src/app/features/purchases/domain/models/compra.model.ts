import { EstadoCompra } from './estado-compra.enum';

export interface ProveedorBasicoResponse {
  id: number;
  nombre: string;
}

export interface EmpleadoBasicoResponse {
  id: number;
  usuarioId: number;
  nombreUsuario: string;
  emailUsuario: string;
  area: string;
  cargo: string;
}

export interface CompraDetalleResponse {
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

export class Compra {
  constructor(
    public readonly id: number,
    public readonly fechaCompra: string,
    public readonly estado: { nombre: EstadoCompra },
    public readonly total: number,
    public readonly proveedor: ProveedorBasicoResponse,
    public readonly empleado: EmpleadoBasicoResponse,
    public readonly detalles: CompraDetalleResponse[]
  ) {}
}

export class CompraResumen {
  constructor(
    public readonly id: number,
    public readonly fechaCompra: string,
    public readonly estado: EstadoCompra,
    public readonly total: number,
    public readonly cantidadItem: number,
    public readonly proveedor: ProveedorBasicoResponse,
    public readonly empleado: EmpleadoBasicoResponse
  ) {}
}
