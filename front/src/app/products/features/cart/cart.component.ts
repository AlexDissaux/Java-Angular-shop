import { Component, computed, inject } from '@angular/core';
import { CartService } from 'app/products/services/cart.service';
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
  imports: [DataViewModule, CardModule, ButtonModule, DialogModule], // need to create ngModule and moce the imports in the module file
  standalone: true, // need to create ngModule and delete this line

})
export class CartComponent {

  private cartService = inject(CartService);

  cart = this.cartService.getCart();
  totalPrice = computed(() => this.cartService.getTotalPrice());

  removeProduct(id: number) {
    this.cartService.removeFromCart(id);
  }

  clearCart() {
    this.cartService.clearCart();
  }
}
