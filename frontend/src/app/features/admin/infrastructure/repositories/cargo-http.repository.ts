import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Cargo } from '../../domain/models/cargo.model';
import { CargoRepository } from '../../domain/repositories/cargo.repository';
import { CargoResponseDto } from '../dtos/admin.dto';
import { AdminMapper } from '../mappers/admin.mapper';

@Injectable({
  providedIn: 'root'
})
export class CargoHttpRepository implements CargoRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/cargos`;

  listarCargos(): Observable<Cargo[]> {
    return this.http.get<CargoResponseDto[]>(this.apiUrl).pipe(
      map(dtos => dtos.map(AdminMapper.toCargo))
    );
  }

  obtenerPorId(id: number): Observable<Cargo> {
    return this.http.get<CargoResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map(AdminMapper.toCargo)
    );
  }

  registrarCargo(nombre: string): Observable<Cargo> {
    return this.http.post<CargoResponseDto>(this.apiUrl, { nombre }).pipe(
      map(AdminMapper.toCargo)
    );
  }

  editarCargo(id: number, nombre: string): Observable<Cargo> {
    return this.http.put<CargoResponseDto>(this.apiUrl, { id, nombre }).pipe(
      map(AdminMapper.toCargo)
    );
  }

  eliminarCargo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
