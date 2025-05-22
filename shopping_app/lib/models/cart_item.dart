import 'package:flutter/foundation.dart';
import 'product.dart';

class CartItem {
  final Product product;
  final int quantity;

  const CartItem({required this.product, this.quantity = 1});

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is CartItem &&
              runtimeType == other.runtimeType &&
              product.id == other.product.id;

  @override
  int get hashCode => product.id.hashCode;
}