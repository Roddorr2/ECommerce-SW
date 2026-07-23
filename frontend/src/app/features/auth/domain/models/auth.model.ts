export class Auth {
  constructor(
    public readonly token: string | null,
    public readonly rol: string | null,
    public readonly nombre: string | null,
    public readonly requiere2FA: boolean,
    public readonly mensaje: string
  ) {}

  public get normalizadoRol(): string | null {
    if (!this.rol) return null;
    return this.rol.charAt(0).toUpperCase() + this.rol.slice(1).toLowerCase();
  }
}
