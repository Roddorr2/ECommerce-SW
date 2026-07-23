import {Component, computed, DestroyRef, effect, inject, OnInit, signal} from '@angular/core';
import {NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router} from '@angular/router';
import {filter} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-page-transition',
  imports: [],
  templateUrl: './page-transition.component.html',
  styleUrl: './page-transition.component.scss'
})
export class PageTransitionComponent {
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  public cargando = signal<boolean>(false);
  public progreso = signal<number>(0);
  public mensaje = signal<string>('Cargando...');

  public anchoProgreso = computed(() => `${this.progreso()}%`);
  public mostrarBarraProgreso = computed(() => this.cargando() && this.progreso() > 0 && this.progreso() < 100);
  public mostrarSpinner = computed(() => this.cargando() && this.progreso() === 0);

  constructor() {
    this.initProgressEffect();
  }

  ngOnInit(): void {
    this.initRouterEvents();
  }

  private initRouterEvents(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationStart),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.iniciarTransicion();
    });

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.completarTransicion();
    });

    this.router.events.pipe(
      filter(event => event instanceof NavigationCancel || event instanceof NavigationError),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.completarTransicion();
    });
  }

  private initProgressEffect(): void {
    effect(() => {
      if (this.cargando()) {
        this.simularProgreso();
      }
    }, { allowSignalWrites: true });
  }

  private simularProgreso(): void {
    this.progreso.set(0);
    this.mensaje.set('Iniciando...');

    const intervalo = setInterval(() => {
      if (!this.cargando()) {
        clearInterval(intervalo);
        return;
      }

      const progresoActual = this.progreso();
      if (progresoActual < 90) {
        const incremento = progresoActual < 30 ? 5 : (progresoActual < 60 ? 3 : 2);
        this.progreso.set(Math.min(progresoActual + incremento, 90));

        if (progresoActual >= 70) {
          this.mensaje.set('Finalizando...');
        } else if (progresoActual >= 40) {
          this.mensaje.set('Cargando recursos...');
        } else if (progresoActual >= 10) {
          this.mensaje.set('Preparando página...');
        }
      }
    }, 50);

    const intervaloVerificacion = setInterval(() => {
      if (!this.cargando()) {
        clearInterval(intervalo);
        clearInterval(intervaloVerificacion);
      }
    }, 100);
  }

  public iniciarTransicion(): void {
    this.cargando.set(true);
    this.progreso.set(0);
    this.mensaje.set('Cargando...');
  }

  public completarTransicion(): void {
    this.progreso.set(100);
    this.mensaje.set('Completado');

    setTimeout(() => {
      this.cargando.set(false);
      this.progreso.set(0);
    }, 200);
  }

  public ocultarForzado(): void {
    this.cargando.set(false);
    this.progreso.set(0);
  }

  public async conTransicion<T>(callback: () => Promise<T>): Promise<T> {
    this.iniciarTransicion();
    try {
      const resultado = await callback();
      this.completarTransicion();
      return resultado;
    } catch (error) {
      this.completarTransicion();
      throw error;
    }
  }
}
