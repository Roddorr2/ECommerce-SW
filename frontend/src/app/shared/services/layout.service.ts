import { computed, DestroyRef, inject, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LayoutService {
  private destroyRef = inject(DestroyRef);

  public sidebarVisible = signal<boolean>(true);
  public isMobile = signal<boolean>(false);
  public isTablet = signal<boolean>(false);

  public mostrarOverlay = computed(
    () => this.sidebarVisible() && (this.isMobile() || this.isTablet()),
  );

  public sidebarClase = computed(() => ({
    visible: this.sidebarVisible(),
    mobile: this.isMobile(),
    tablet: this.isTablet(),
  }));

  public contentExpanded = computed(() => !this.sidebarVisible());

  constructor() {
    this.checkScreenSize();
    this.initResizeListener();
  }

  private initResizeListener(): void {
    window.addEventListener('resize', () => this.checkScreenSize());
  }

  public checkScreenSize(): void {
    const width = window.innerWidth;
    this.isMobile.set(width < 768);
    this.isTablet.set(width >= 768 && width < 992);

    if (this.isMobile() || this.isTablet()) {
      this.sidebarVisible.set(false);
    } else {
      this.sidebarVisible.set(true);
    }
  }

  public toggleSidebar(): void {
    this.sidebarVisible.set(!this.sidebarVisible());
  }

  public openSidebar(): void {
    this.sidebarVisible.set(true);
  }

  public closeSidebar(): void {
    if (this.isMobile() || this.isTablet()) {
      this.sidebarVisible.set(false);
    }
  }
}
