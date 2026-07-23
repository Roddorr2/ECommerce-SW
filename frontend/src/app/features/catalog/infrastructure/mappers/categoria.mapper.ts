import { Categoria } from '../../domain/models/categoria.model';
import { CategoriaResponseDto } from '../dtos/categoria.dto';

export class CategoriaMapper {
  public static toDomain(dto: CategoriaResponseDto): Categoria {
    return new Categoria(dto.id, dto.nombre);
  }

  public static toDomainList(dtos: CategoriaResponseDto[]): Categoria[] {
    return dtos.map(dto => this.toDomain(dto));
  }
}
