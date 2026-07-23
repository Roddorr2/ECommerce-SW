export class Proveedor {
  constructor(
    public readonly id: number,
    public readonly nombre: string,
    public readonly telefono: string,
    public readonly email: string,
    public readonly direccion: string,
    public readonly tipoProveedor: string
  ) {}

  public isValid(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return (
      !!this.nombre && this.nombre.trim().length <= 50 &&
      !!this.telefono && this.telefono.trim().length <= 20 &&
      !!this.email && emailRegex.test(this.email) && this.email.trim().length <= 100 &&
      (!this.direccion || this.direccion.trim().length <= 100)
    );
  }
}
