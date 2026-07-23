import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrollRevealComponent } from './scroll-reveal.component';

describe('ScrollRevealComponent', () => {
  let component: ScrollRevealComponent;
  let fixture: ComponentFixture<ScrollRevealComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScrollRevealComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScrollRevealComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
