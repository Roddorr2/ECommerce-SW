import { Producto } from '../../domain/models/producto.model';
import { ProductoResponseDto, CrearProductoRequestDto, ActualizarProductoRequestDto } from '../dtos/producto.dto';
import { RegistrarProductoData, EditarProductoData } from '../../domain/repositories/producto.repository';

export class ProductoMapper {
  public static toDomain(dto: ProductoResponseDto): Producto {
    return new Producto(
      dto.id,
      dto.nombre,
      dto.sku,
      dto.descripcion,
      dto.precio,
      dto.stock,
      dto.imagenNombre || null,
      dto.activo,
      dto.categoriaId,
      dto.categoria
    );
  }

  public static toDomainList(dtos: ProductoResponseDto[]): Producto[] {
    return dtos.map((dto) => this.toDomain(dto));
  }

  public static toCreateRequest(data: RegistrarProductoData): CrearProductoRequestDto {
    return {
      nombre: data.nombre,
      sku: data.sku,
      descripcion: data.descripcion,
      precio: data.precio,
      categoriaId: data.categoriaId,
      imagenNombre: data.imagenNombre,
    };
  }

  public static toUpdateRequest(data: EditarProductoData): ActualizarProductoRequestDto {
    return {
      id: data.id,
      nombre: data.nombre,
      sku: data.sku,
      descripcion: data.descripcion,
      precio: data.precio,
      categoriaId: data.categoriaId,
      imagenNombre: data.imagenNombre,
      activo: data.activo,
    };
  }
}
