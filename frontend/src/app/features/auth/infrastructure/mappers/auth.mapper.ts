import { Auth } from '../../domain/models/auth.model';
import { AuthResponseDto } from '../dtos/auth.dto';

export class AuthMapper {
  public static toDomain(dto: AuthResponseDto): Auth {
    return new Auth(
      dto.token,
      dto.rol,
      dto.nombre,
      dto.requiere2FA,
      dto.mensaje
    );
  }
}
