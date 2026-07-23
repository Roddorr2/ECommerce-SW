export class MovimientoStock {
  constructor(
    public readonly id: number,
    public readonly producto: string,
    public readonly sku: string,
    public readonly cantidadAnterior: number,
    public readonly cantidadNueva: number,
    public readonly tipoMovimiento: string,
    public readonly codigoReferencia: string,
    public readonly usuario: string,
    public readonly fechaMovimiento: string,
    public readonly observacion: string
  ) {}

  public get diferencia(): number {
    return this.cantidadNueva - this.cantidadAnterior;
  }

  public get isEntrada(): boolean {
    return this.tipoMovimiento === 'ENTRADA' || this.tipoMovimiento === 'entrada';
  }

  public get isSalida(): boolean {
    return this.tipoMovimiento === 'SALIDA' || this.tipoMovimiento === 'salida';
  }

  public get descripcion(): string {
    const tipo = this.isEntrada ? 'Entrada' : 'Salida';
    const cant = Math.abs(this.diferencia);
    return `${tipo} de ${cant} unidades (${this.cantidadAnterior} → ${this.cantidadNueva})`;
  }
}
