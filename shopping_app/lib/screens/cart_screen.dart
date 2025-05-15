import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/cart_provider.dart';

class CartScreen extends ConsumerWidget {
  const CartScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final cartItems = ref.watch(cartProvider);
    final totalPrice = ref.watch(cartProvider.notifier).totalPrice;

    return Scaffold(
      appBar: AppBar(
        title: Image.asset('assets/background_icon.png', height: 40),
      ),
      body: Container(
        decoration: BoxDecoration(
          image: DecorationImage(
            image: const AssetImage('assets/background_icon.png'),
            fit: BoxFit.cover,
            colorFilter: ColorFilter.mode(Colors.white.withOpacity(0.3), BlendMode.srcOver),
          ),
        ),
        child: cartItems.isEmpty
            ? const Center(
            child: Text('Giỏ hàng trống', style: TextStyle(fontSize: 18, color: Colors.black)))
            : Column(
          children: [
            Expanded(
              child: ListView.builder(
                padding: const EdgeInsets.all(16),
                itemCount: cartItems.length,
                itemBuilder: (context, index) {
                  final cartItem = cartItems[index];
                  return Card(
                    elevation: 4,
                    child: ListTile(
                      leading: ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: Image.network(
                          cartItem.product.imageUrl,
                          width: 50,
                          height: 50,
                          fit: BoxFit.cover,
                          errorBuilder: (context, error, stackTrace) => const Icon(Icons.error),
                        ),
                      ),
                      title: Text(cartItem.product.name,
                          style: const TextStyle(color: Colors.black, fontWeight: FontWeight.w600)),
                      subtitle: Text('${cartItem.product.price.toStringAsFixed(0)} VNĐ',
                          style: const TextStyle(color: Colors.red)),
                      trailing: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          IconButton(
                            icon: const Icon(Icons.remove, color: Colors.black),
                            onPressed: () {
                              ref.read(cartProvider.notifier).updateQuantity(
                                cartItem.product.id,
                                cartItem.quantity - 1,
                              );
                            },
                          ),
                          Text('${cartItem.quantity}',
                              style: const TextStyle(color: Colors.black, fontSize: 16)),
                          IconButton(
                            icon: const Icon(Icons.add, color: Colors.black),
                            onPressed: () {
                              ref.read(cartProvider.notifier).updateQuantity(
                                cartItem.product.id,
                                cartItem.quantity + 1,
                              );
                            },
                          ),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                children: [
                  Text(
                    'Tổng tiền: ${totalPrice.toStringAsFixed(0)} VNĐ',
                    style: const TextStyle(
                      fontSize: 20,
                      color: Colors.red,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 16),
                  SizedBox(
                    width: 200,
                    child: ElevatedButton(
                      onPressed: () {
                        Navigator.pushNamed(context, '/checkout');
                      },
                      child: const Text('Tiến hành thanh toán'),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}