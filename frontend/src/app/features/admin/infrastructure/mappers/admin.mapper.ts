import { Area } from '../../domain/models/area.model';
import { Cargo } from '../../domain/models/cargo.model';
import { Empleado } from '../../domain/models/empleado.model';
import { Rol } from '../../domain/models/rol.model';
import { Usuario } from '../../domain/models/usuario.model';
import {
  AreaResponseDto,
  CargoResponseDto,
  EmpleadoResponseDto,
  RolResponseDto,
  UsuarioResponseDto
} from '../dtos/admin.dto';

export class AdminMapper {
  public static toArea(dto: AreaResponseDto): Area {
    return new Area(dto.id, dto.nombre);
  }

  public static toCargo(dto: CargoResponseDto): Cargo {
    return new Cargo(dto.id, dto.nombre);
  }

  public static toEmpleado(dto: EmpleadoResponseDto): Empleado {
    return new Empleado(
      dto.id,
      dto.usuarioId,
      dto.nombreUsuario,
      dto.emailUsuario,
      dto.area,
      dto.cargo
    );
  }

  public static toRol(dto: RolResponseDto): Rol {
    return new Rol(dto.id, dto.nombre);
  }

  public static toUsuario(dto: UsuarioResponseDto): Usuario {
    return new Usuario(
      dto.id,
      dto.nombre,
      dto.email,
      dto.estado,
      dto.rol,
      dto.dobleFactorActivo
    );
  }
}
