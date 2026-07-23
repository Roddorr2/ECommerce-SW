export class Categoria {
  constructor(
    public readonly id: number,
    public readonly nombre: string
  ) {}

  // Domain behavior/helpers (if needed in the future)
  public isValid(): boolean {
    return !!this.nombre && this.nombre.trim().length >= 3 && this.nombre.trim().length <= 50;
  }
}
