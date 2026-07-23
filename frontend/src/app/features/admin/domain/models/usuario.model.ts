export class Usuario {
  constructor(
    public readonly id: number,
    public readonly nombre: string,
    public readonly email: string,
    public readonly estado: boolean,
    public readonly rol: string,
    public readonly dobleFactorActivo: boolean
  ) {}
}
