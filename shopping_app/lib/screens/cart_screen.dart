import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/cart_provider.dart';

class CartScreen extends ConsumerWidget {
  const CartScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final cartItems = ref.watch(cartProvider);
    final cartNotifier = ref.read(cartProvider.notifier);
    final totalPrice = ref.watch(cartProvider.notifier).totalPrice;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Giỏ hàng'),
      ),
      body: cartItems.isEmpty
          ? const Center(child: Text('Giỏ hàng trống'))
          : ListView.builder(
        padding: const EdgeInsets.all(8.0),
        itemCount: cartItems.length,
        itemBuilder: (context, index) {
          final item = cartItems[index];
          return Card(
            child: ListTile(
              leading: Image.network(
                item.product.imageUrl,
                width: 50,
                height: 50,
                fit: BoxFit.cover,
              ),
              title: Text(item.product.name),
              subtitle: Text('\$${item.product.price} x ${item.quantity}'),
              trailing: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  IconButton(
                    icon: const Icon(Icons.remove),
                    onPressed: () => cartNotifier.updateQuantity(
                        item.product.id, item.quantity - 1),
                  ),
                  Text('${item.quantity}'),
                  IconButton(
                    icon: const Icon(Icons.add),
                    onPressed: () => cartNotifier.updateQuantity(
                        item.product.id, item.quantity + 1),
                  ),
                ],
              ),
            ),
          );
        },
      ),
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              'Tổng: \$${totalPrice.toStringAsFixed(2)}',
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            ElevatedButton(
              onPressed: cartItems.isEmpty
                  ? null
                  : () => Navigator.pushNamed(context, '/checkout'),
              child: const Text('Thanh toán'),
            ),
          ],
        ),
      ),
    );
  }
}