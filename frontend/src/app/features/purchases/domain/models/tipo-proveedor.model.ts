export class TipoProveedor {
  constructor(
    public readonly id: number,
    public readonly nombre: string
  ) {}

  public isValid(): boolean {
    return !!this.nombre && this.nombre.trim().length >= 3 && this.nombre.trim().length <= 50;
  }
}
