export interface CarritoItem {
  id: number;
  productoId: number;
  productoNombre: string;
  productoSku: string;
  precioUnitario: number;
  cantidad: number;
  subtotal: number;
  imagenNombre: string;
}

export class Carrito {
  constructor(
    public readonly id: number,
    public readonly items: CarritoItem[],
    public readonly total: number
  ) {}
}
