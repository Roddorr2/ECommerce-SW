import { Component } from '@angular/core';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';

@Component({
  selector: 'app-politicas-envio',
  standalone: true,
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './politicas-envio.component.html',
  styleUrl: './politicas-envio.component.scss'
})
export class PoliticasEnvioComponent {
}
