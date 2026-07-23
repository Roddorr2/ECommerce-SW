export class Cliente {
  constructor(
    public readonly id: number,
    public readonly usuarioId: number,
    public readonly usuario: string,
    public readonly correo: string,
    public readonly telefono: string,
    public readonly direccion: string
  ) {}
}
