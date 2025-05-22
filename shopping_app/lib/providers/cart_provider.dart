import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/cart_item.dart';
import '../models/product.dart';

final cartProvider = StateNotifierProvider<CartNotifier, List<CartItem>>((ref) {
  return CartNotifier();
});

class CartNotifier extends StateNotifier<List<CartItem>> {
  CartNotifier() : super([]);

  void addToCart(Product product) {
    if (product == null) return; // Kiểm tra product không null
    final existingItemIndex = state.indexWhere((item) => item.product.id == product.id);

    if (existingItemIndex != -1) {
      state = [
        for (int i = 0; i < state.length; i++)
          if (i == existingItemIndex)
            CartItem(product: state[i].product, quantity: state[i].quantity + 1)
          else
            state[i]
      ];
    } else {
      state = [...state, CartItem(product: product, quantity: 1)];
    }
  }

  void removeFromCart(CartItem cartItem) {
    state = state.where((item) => item != cartItem).toList();
  }

  void updateQuantity(CartItem cartItem, int quantity) {
    if (quantity <= 0) {
      removeFromCart(cartItem);
    } else {
      state = [
        for (final item in state)
          if (item == cartItem)
            CartItem(product: item.product, quantity: quantity)
          else
            item
      ];
    }
  }

  void clearCart() {
    state = [];
  }

  double get totalPrice {
    return state.fold(
      0,
          (sum, item) => sum + (item.product.price * item.quantity),
    );
  }
}