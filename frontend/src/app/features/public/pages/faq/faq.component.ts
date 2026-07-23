import { Component, signal } from '@angular/core';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { CommonModule } from '@angular/common';

interface FaqItem {
  pregunta: string;
  respuesta: string;
  categoria: string;
  abierta: boolean;
}

@Component({
  selector: 'app-faq',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './faq.component.html',
  styleUrl: './faq.component.scss'
})
export class FaqComponent {
  public categoriaSeleccionada = signal<string>('todos');

  public faqs = signal<FaqItem[]>([
    {
      categoria: 'general',
      pregunta: '¿Qué es Technology Fix y qué productos ofrecen?',
      respuesta: 'Technology Fix es una plataforma de comercio electrónico líder en tecnología. Ofrecemos hardware para PC, laptops, periféricos, componentes de red, accesorios para gaming y servicio técnico especializado, todo respaldado por garantía oficial del fabricante.',
      abierta: false
    },
    {
      categoria: 'pedidos',
      pregunta: '¿Cómo puedo realizar un pedido en la plataforma?',
      respuesta: 'Para realizar un pedido, primero debes crearte una cuenta e iniciar sesión. Luego, navega por nuestro catálogo, añade los productos que desees al carrito, especifica tu dirección de entrega y procede al pago. Recibirás un correo electrónico de confirmación con el detalle de tu compra.',
      abierta: false
    },
    {
      categoria: 'pedidos',
      pregunta: '¿Puedo modificar o cancelar mi pedido después de pagarlo?',
      respuesta: 'Si tu pedido aún no ha entrado en la etapa de preparación (dentro de las primeras 2 horas posteriores al pago), puedes solicitar la cancelación o modificación poniéndote en contacto de inmediato con nuestro soporte técnico.',
      abierta: false
    },
    {
      categoria: 'envios',
      pregunta: '¿A qué zonas realizan envíos y cuál es el costo?',
      respuesta: 'Realizamos envíos a todo el territorio nacional (Perú). El costo de envío varía según la provincia y distrito de destino, así como el peso y volumen del paquete. El costo total se calcula automáticamente antes de finalizar tu compra.',
      abierta: false
    },
    {
      categoria: 'envios',
      pregunta: '¿Cuánto tiempo tardará en llegar mi pedido?',
      respuesta: 'Para Lima Metropolitana, el plazo de entrega es de 24 a 48 horas hábiles. Para provincias, el tiempo estimado es de 3 a 5 días hábiles, sujeto a la accesibilidad del transporte terrestre o aéreo.',
      abierta: false
    },
    {
      categoria: 'pagos',
      pregunta: '¿Cuáles son los métodos de pago aceptados?',
      respuesta: 'Aceptamos tarjetas de crédito y débito (Visa, Mastercard, American Express, Diners Club), transferencias bancarias directas (BCP, BBVA, Interbank) y pagos rápidos digitales a través de Yape o Plin.',
      abierta: false
    },
    {
      categoria: 'garantia',
      pregunta: '¿Los productos cuentan con garantía?',
      respuesta: 'Sí, todos nuestros productos son 100% originales y cuentan con garantía oficial de fábrica que varía entre 1 y 3 años (según el componente). Además, Technology Fix ofrece una garantía de cambio rápido de 7 días por fallas de fábrica.',
      abierta: false
    },
    {
      categoria: 'soporte',
      pregunta: '¿Cómo puedo contactar al área de soporte técnico?',
      respuesta: 'Puedes ingresar a la sección de Soporte Técnico en el footer y rellenar el formulario de contacto, o enviarnos un correo a technologyfix@gmail.com. Nuestro equipo te responderá en un plazo máximo de 24 horas.',
      abierta: false
    }
  ]);

  public seleccionarCategoria(categoria: string): void {
    this.categoriaSeleccionada.set(categoria);
  }

  public toggleFaq(index: number, faqsFiltradas: FaqItem[]): void {
    const itemFiltrado = faqsFiltradas[index];
    const listaOriginal = this.faqs();
    
    const indexOriginal = listaOriginal.findIndex(
      item => item.pregunta === itemFiltrado.pregunta
    );

    if (indexOriginal !== -1) {
      const nuevoEstado = [...listaOriginal];
      nuevoEstado[indexOriginal] = {
        ...nuevoEstado[indexOriginal],
        abierta: !nuevoEstado[indexOriginal].abierta
      };
      this.faqs.set(nuevoEstado);
    }
  }

  public obtenerFaqsFiltradas(): FaqItem[] {
    const cat = this.categoriaSeleccionada();
    if (cat === 'todos') {
      return this.faqs();
    }
    return this.faqs().filter(faq => faq.categoria === cat);
  }
}
