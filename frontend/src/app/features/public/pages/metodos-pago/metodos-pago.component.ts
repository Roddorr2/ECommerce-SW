import { Component } from '@angular/core';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';

@Component({
  selector: 'app-metodos-pago',
  standalone: true,
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './metodos-pago.component.html',
  styleUrl: './metodos-pago.component.scss'
})
export class MetodosPagoComponent {
}
