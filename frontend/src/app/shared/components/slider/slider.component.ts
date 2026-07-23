import { Component, computed, input, OnInit, output, signal } from '@angular/core';

export interface SlideItem {
  imagen: string;
  titulo: string;
  subtitulo?: string;
  link?: string;
}

@Component({
  selector: 'app-slider',
  standalone: true,
  imports: [],
  templateUrl: './slider.component.html',
  styleUrl: './slider.component.scss',
})
export class SliderComponent implements OnInit {
  public slides = input<SlideItem[]>([], { alias: 'slides' });
  public autoPlay = input<boolean>(true);
  public intervalo = input<number>(5000);
  public mostrarIndicadores = input<boolean>(true);
  public mostrarControles = input<boolean>(true);

  public slideChange = output<number>();
  public slideClick = output<SlideItem>();

  public indiceActual = signal<number>(0);
  public estaPausado = signal<boolean>(false);
  public touchStartX = signal<number>(0);
  public touchEndX = signal<number>(0);

  public totalSlides = computed(() => this.slides().length);
  public tieneSlides = computed(() => this.totalSlides() > 0);
  public slideActual = computed(() => this.slides()[this.indiceActual()]);
  public porcentajeProgreso = computed(() => {
    if (!this.autoPlay() || this.estaPausado()) return 0;
    return this.progresoActual();
  });

  private intervaloId: number | null = null;
  private progresoActual = signal<number>(0);
  private progresoIntervaloId: number | null = null;

  ngOnInit(): void {
    if (this.autoPlay() && this.tieneSlides()) {
      this.iniciarAutoPlay();
    }
  }

  ngOnDestroy(): void {
    this.detenerAutoPlay();
  }

  private iniciarAutoPlay(): void {
    if (this.intervaloId) return;

    this.intervaloId = window.setInterval(() => {
      if (!this.estaPausado()) {
        this.siguiente();
      }
    }, this.intervalo());

    this.iniciarProgreso();
  }

  private iniciarProgreso(): void {
    this.progresoActual.set(0);
    const paso = 100 / (this.intervalo() / 100);

    this.progresoIntervaloId = window.setInterval(() => {
      if (!this.estaPausado() && this.autoPlay()) {
        const nuevoProgreso = this.progresoActual() + paso;
        if (nuevoProgreso >= 100) {
          this.progresoActual.set(100);
        } else {
          this.progresoActual.set(nuevoProgreso);
        }
      }
    }, 100);
  }

  private detenerAutoPlay(): void {
    if (this.intervaloId) {
      clearInterval(this.intervaloId);
      this.intervaloId = null;
    }
    if (this.progresoIntervaloId) {
      clearInterval(this.progresoIntervaloId);
      this.progresoIntervaloId = null;
    }
  }

  public anterior(): void {
    if (!this.tieneSlides()) return;

    const nuevoIndice =
      this.indiceActual() === 0 ? this.totalSlides() - 1 : this.indiceActual() - 1;

    this.irASlide(nuevoIndice);
  }

  public siguiente(): void {
    if (!this.tieneSlides()) return;

    const nuevoIndice =
      this.indiceActual() === this.totalSlides() - 1 ? 0 : this.indiceActual() + 1;

    this.irASlide(nuevoIndice);
  }

  public irASlide(indice: number): void {
    if (indice === this.indiceActual()) return;
    if (indice < 0 || indice >= this.totalSlides()) return;

    this.indiceActual.set(indice);
    this.progresoActual.set(0);
    this.slideChange.emit(indice);
  }

  public pausar(): void {
    if (!this.autoPlay()) return;
    this.estaPausado.set(true);
  }

  public reanudar(): void {
    if (!this.autoPlay()) return;
    this.estaPausado.set(false);
    this.progresoActual.set(0);
  }

  public togglePausa(): void {
    if (this.estaPausado()) {
      this.reanudar();
    } else {
      this.pausar();
    }
  }

  public onSlideClick(): void {
    const slide = this.slideActual();
    if (slide) {
      this.slideClick.emit(slide);
      if (slide.link) {
        window.open(slide.link, '_blank');
      }
    }
  }

  // Eventos táctiles para swipe
  public onTouchStart(event: TouchEvent): void {
    this.touchStartX.set(event.touches[0].clientX);
  }

  public onTouchEnd(event: TouchEvent): void {
    this.touchEndX.set(event.changedTouches[0].clientX);
    this.procesarSwipe();
  }

  private procesarSwipe(): void {
    const diferencia = this.touchStartX() - this.touchEndX();
    if (Math.abs(diferencia) < 50) return;

    if (diferencia > 0) {
      this.siguiente();
    } else {
      this.anterior();
    }
  }
}
