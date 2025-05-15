import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product.dart';

class CartItem {
  final Product product;
  int quantity;

  CartItem({required this.product, this.quantity = 1});
}

class CartNotifier extends StateNotifier<List<CartItem>> {
  CartNotifier() : super([]);

  double get totalPrice =>
      state.fold(0, (sum, item) => sum + item.product.price * item.quantity);

  void addToCart(Product product) {
    final existingItemIndex =
    state.indexWhere((item) => item.product.id == product.id);
    if (existingItemIndex >= 0) {
      final updatedItems = [...state];
      updatedItems[existingItemIndex] = CartItem(
        product: state[existingItemIndex].product,
        quantity: state[existingItemIndex].quantity + 1,
      );
      state = updatedItems;
    } else {
      state = [...state, CartItem(product: product)];
    }
  }

  void updateQuantity(String productId, int quantity) {
    if (quantity <= 0) {
      state = state.where((item) => item.product.id != productId).toList();
    } else {
      final updatedItems = [...state];
      final index = updatedItems.indexWhere((item) => item.product.id == productId);
      updatedItems[index] = CartItem(
        product: updatedItems[index].product,
        quantity: quantity,
      );
      state = updatedItems;
    }
  }

  void clearCart() {
    state = [];
  }
}

final cartProvider = StateNotifierProvider<CartNotifier, List<CartItem>>(
      (ref) => CartNotifier(),
);