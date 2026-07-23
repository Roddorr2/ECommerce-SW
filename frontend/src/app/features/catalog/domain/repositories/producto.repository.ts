import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';

export interface RegistrarProductoData {
  nombre: string;
  sku: string;
  descripcion: string;
  precio: number;
  categoriaId: number;
  imagenNombre?: string;
}

export interface EditarProductoData {
  id: number;
  nombre: string;
  sku: string;
  descripcion: string;
  precio: number;
  categoriaId: number;
  imagenNombre?: string;
  activo: boolean;
}

export interface ProductoRepository {
  listarProductos(): Observable<Producto[]>;
  obtenerPorId(id: number): Observable<Producto>;
  registrarProducto(data: RegistrarProductoData): Observable<Producto>;
  editarProducto(data: EditarProductoData): Observable<Producto>;
  eliminarProducto(id: number): Observable<void>;
  subirImagen(archivo: File): Observable<{ nombreArchivo: string }>;
  buscarProductosDisponibles(nombre: string): Observable<Producto[]>;
  obtenerUrlImagen(imagenNombre: string): string;
}

export const PRODUCTO_REPOSITORY = new InjectionToken<ProductoRepository>('ProductoRepository');
