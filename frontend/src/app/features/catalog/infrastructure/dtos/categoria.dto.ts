export interface CategoriaResponseDto {
  id: number;
  nombre: string;
}

export interface CrearCategoriaRequestDto {
  nombre: string;
}

export interface ActualizarCategoriaRequestDto {
  id: number;
  nombre: string;
}
