import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shopping_app/models/cart_item.dart';
import 'package:shopping_app/models/product.dart';

final cartProvider = StateNotifierProvider<CartNotifier, List<CartItem>>((ref) {
  return CartNotifier();
});

class CartNotifier extends StateNotifier<List<CartItem>> {
  CartNotifier() : super([]);

  void addToCart(Product product) {
    final existingItem = state.firstWhere(
          (item) => item.product.id == product.id,
      orElse: () => CartItem(product: product),
    );

    if (state.contains(existingItem)) {
      state = [
        for (final item in state)
          if (item.product.id == product.id)
            CartItem(product: item.product, quantity: item.quantity + 1)
          else
            item
      ];
    } else {
      state = [...state, existingItem];
    }
  }

  void removeFromCart(int productId) {
    state = state.where((item) => item.product.id != productId).toList();
  }

  void updateQuantity(int productId, int quantity) {
    if (quantity <= 0) {
      removeFromCart(productId);
    } else {
      state = [
        for (final item in state)
          if (item.product.id == productId)
            CartItem(product: item.product, quantity: quantity)
          else
            item
      ];
    }
  }

  double get totalPrice {
    return state.fold(
      0,
          (sum, item) => sum + (item.product.price * item.quantity),
    );
  }
}