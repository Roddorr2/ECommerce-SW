export class Producto {
  constructor(
    public readonly id: number,
    public readonly nombre: string,
    public readonly sku: string,
    public readonly descripcion: string,
    public readonly precio: number,
    public readonly stock: number,
    public readonly imagenNombre: string | null,
    public readonly activo: boolean,
    public readonly categoriaId: number,
    public readonly categoria?: string
  ) {}

  public isValid(): boolean {
    return (
      !!this.nombre &&
      this.nombre.trim().length >= 3 &&
      this.nombre.trim().length <= 150 &&
      !!this.sku &&
      this.precio > 0 &&
      this.categoriaId > 0
    );
  }

  public get hasImagen(): boolean {
    return !!this.imagenNombre;
  }
}
