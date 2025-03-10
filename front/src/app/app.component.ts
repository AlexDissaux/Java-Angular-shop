import {
  Component,
  computed,
  inject,
} from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { SplitterModule } from 'primeng/splitter';
import { ToolbarModule } from 'primeng/toolbar';
import { PanelMenuComponent } from "./shared/ui/panel-menu/panel-menu.component";
import { CartService } from "./products/services/cart.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  standalone: true,
  imports: [RouterModule, SplitterModule, ToolbarModule, PanelMenuComponent],
})
export class AppComponent {
  private router = inject(Router); // Using inject instead of constructor
  private cartService = inject(CartService); 
  cartSize = computed(() => this.cartService.getSize())

  title = "ALTEN SHOP";
  navigateToCart() {
    this.router.navigate(['/cart']);
  }
}
