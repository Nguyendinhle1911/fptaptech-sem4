import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/cart_item.dart';

class Order {
  final List<CartItem> orderItems;
  final double totalPrice;
  final String recipientName;
  final String recipientPhone;
  final String recipientAddress;
  final String recipientEmail;
  final DateTime orderDate;

  Order({
    required this.orderItems,
    required this.totalPrice,
    required this.recipientName,
    required this.recipientPhone,
    required this.recipientAddress,
    this.recipientEmail = '',
    required this.orderDate,
  });
}

final orderHistoryProvider = StateNotifierProvider<OrderHistoryNotifier, List<Order>>((ref) {
  return OrderHistoryNotifier();
});

class OrderHistoryNotifier extends StateNotifier<List<Order>> {
  OrderHistoryNotifier() : super([]);

  void addOrder({
    required List<CartItem> orderItems,
    required double totalPrice,
    required String recipientName,
    required String recipientPhone,
    required String recipientAddress,
    String recipientEmail = '',
    required DateTime orderDate,
  }) {
    state = [
      ...state,
      Order(
        orderItems: orderItems,
        totalPrice: totalPrice,
        recipientName: recipientName,
        recipientPhone: recipientPhone,
        recipientAddress: recipientAddress,
        recipientEmail: recipientEmail,
        orderDate: orderDate,
      ),
    ];
  }
}