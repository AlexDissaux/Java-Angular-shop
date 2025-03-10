import { computed, Injectable, signal } from '@angular/core';
import { Product } from '../data-access/product.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cart = signal<Product[]>([]);
  cartSize = computed(() => this.getCart().length);

  constructor() {
    this.loadCartFromStorage();
  }

  getCart() {
    return this.cart;
  }

  addToCart(product: Product) {
    const currentCart = this.cart();
    const existingProduct = currentCart.find(p => p.id === product.id);

    if (existingProduct) {
      existingProduct.quantity += 1;
    } else {
      currentCart.push({ ...product, quantity: 1 });
    }

    this.cart.set([...currentCart]);
    this.saveCartToStorage();
  }

  removeFromCart(productId: number) {
    const updatedCart = this.cart().filter(p => p.id !== productId);
    this.cart.set(updatedCart);
    this.saveCartToStorage();
  }

  clearCart() {
    this.cart.set([]);
    localStorage.removeItem('cart');
  }

  getSize(): number {
    return this.cart().length
  }

  getTotalPrice(): number {
    return this.cart().reduce((total, item) => total + item.price * item.quantity, 0);
  }

  private saveCartToStorage() {
    localStorage.setItem('cart', JSON.stringify(this.cart()));
  }

  private loadCartFromStorage() {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart.set(JSON.parse(storedCart));
    }
  }
}
