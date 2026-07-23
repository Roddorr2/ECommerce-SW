export interface ProductoResponseDto {
  id: number;
  nombre: string;
  sku: string;
  descripcion: string;
  precio: number;
  stock: number;
  imagenNombre: string;
  activo: boolean;
  categoriaId: number;
  categoria: string;
}

export interface CrearProductoRequestDto {
  nombre: string;
  sku: string;
  descripcion: string;
  precio: number;
  categoriaId: number;
  imagenNombre?: string;
}

export interface ActualizarProductoRequestDto {
  id: number;
  nombre: string;
  sku: string;
  descripcion: string;
  precio: number;
  categoriaId: number;
  imagenNombre?: string;
  activo: boolean;
}

export interface ProductoImagenResponseDto {
  nombreArchivo: string;
}
