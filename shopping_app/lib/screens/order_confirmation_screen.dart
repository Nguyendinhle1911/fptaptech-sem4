import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/cart_item.dart';

class OrderConfirmationScreen extends StatelessWidget {
  final List<CartItem> orderItems;
  final double totalPrice;
  final String recipientName;
  final String recipientPhone;
  final String recipientAddress;
  final DateTime orderDate;

  const OrderConfirmationScreen({
    super.key,
    required this.orderItems,
    required this.totalPrice,
    required this.recipientName,
    required this.recipientPhone,
    required this.recipientAddress,
    required this.orderDate,
  });

  @override
  Widget build(BuildContext context) {
    final orderTime = DateFormat('hh:mm a, dd/MM/yyyy').format(orderDate);

    return Scaffold(
      backgroundColor: const Color(0xFFF5F5F5),
      appBar: AppBar(
        title: Image.asset('assets/background_icon.png', height: 40),
        centerTitle: true,
        backgroundColor: const Color(0xFFEE4D2D),
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.history, color: Colors.white),
            onPressed: () => Navigator.pushNamed(context, '/order-history'),
            tooltip: 'Xem lịch sử đơn hàng',
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            _buildTitle('chi tiết đơn hàng đã đặt'),
            _buildCombinedInfo(orderTime),
            _buildActionButton(context),
          ],
        ),
      ),
    );
  }

  Widget _buildTitle(String title) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(16),
      color: const Color(0xFFEE4D2D),
      child: Text(
        title,
        style: const TextStyle(
          fontSize: 20,
          fontWeight: FontWeight.bold,
          color: Colors.white,
        ),
      ),
    );
  }

  Widget _buildCombinedInfo(String orderTime) {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(16),
      decoration: _cardStyle(),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Order Details Section
          const Text(
            'Chi tiết đơn hàng',
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),
          ...orderItems.map((cartItem) => _buildCartItem(cartItem)).toList(),
          const Divider(height: 32),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('Tổng tiền:',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              Text(
                '${totalPrice.toStringAsFixed(0)} VNĐ',
                style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Colors.red),
              ),
            ],
          ),
          const SizedBox(height: 24), // Spacing between sections

          // Recipient Info Section
          const Text(
            'Thông tin người nhận',
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),
          Text('Họ và tên: $recipientName',
              style: const TextStyle(fontSize: 16)),
          const SizedBox(height: 8),
          Text('Số điện thoại: $recipientPhone',
              style: const TextStyle(fontSize: 16)),
          const SizedBox(height: 8),
          Text('Địa chỉ: $recipientAddress',
              style: const TextStyle(fontSize: 16)),
          const SizedBox(height: 24), // Spacing between sections

          // Time Info Section
          const Text(
            'Thông tin thời gian',
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('Thời gian đặt hàng:',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              Text(orderTime, style: const TextStyle(fontSize: 16)),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildCartItem(CartItem cartItem) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(8),
            child: Image.network(
              cartItem.product.imageUrl,
              width: 60,
              height: 60,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) =>
              const Icon(Icons.error),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  cartItem.product.name,
                  style: const TextStyle(
                      fontWeight: FontWeight.w500, fontSize: 14),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 4),
                Text(
                  'Số lượng: ${cartItem.quantity}',
                  style: TextStyle(fontSize: 12, color: Colors.grey.shade700),
                ),
                const SizedBox(height: 4),
                Text(
                  '${(cartItem.product.price * cartItem.quantity).toStringAsFixed(0)} VNĐ',
                  style: const TextStyle(
                      fontSize: 14,
                      color: Colors.red,
                      fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildActionButton(BuildContext context) {
    return Container(
      margin: const EdgeInsets.all(16),
      width: double.infinity,
      child: ElevatedButton(
        onPressed: () =>
            Navigator.popUntil(context, (route) => route.isFirst),
        style: ElevatedButton.styleFrom(
          backgroundColor: const Color(0xFFEE4D2D),
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(vertical: 14),
          shape:
          RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        ),
        child: const Text('Quay về trang chủ', style: TextStyle(fontSize: 16)),
      ),
    );
  }

  BoxDecoration _cardStyle() {
    return BoxDecoration(
      color: Colors.white,
      borderRadius: BorderRadius.circular(16),
      boxShadow: [
        BoxShadow(
          color: Colors.black.withOpacity(0.05),
          blurRadius: 6,
          offset: const Offset(0, 3),
        ),
      ],
    );
  }
}