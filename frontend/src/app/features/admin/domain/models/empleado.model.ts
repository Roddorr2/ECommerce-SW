export class Empleado {
  constructor(
    public readonly id: number,
    public readonly usuarioId: number,
    public readonly nombreUsuario: string,
    public readonly emailUsuario: string,
    public readonly area: string,
    public readonly cargo: string
  ) {}
}
