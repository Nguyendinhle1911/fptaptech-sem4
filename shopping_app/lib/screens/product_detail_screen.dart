import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product.dart';
import '../providers/cart_provider.dart';

class ProductDetailScreen extends ConsumerStatefulWidget {
  const ProductDetailScreen({super.key});

  @override
  ConsumerState<ProductDetailScreen> createState() => _ProductDetailScreenState();
}

class _ProductDetailScreenState extends ConsumerState<ProductDetailScreen> {
  final TextEditingController _commentController = TextEditingController();
  int likeCount = 0;

  @override
  void dispose() {
    _commentController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final product = ModalRoute.of(context)!.settings.arguments as Product;
    final cart = ref.watch(cartProvider);

    final List<String> imageUrls = [
      product.imageUrl,
      product.imageUrl,
      product.imageUrl,
    ];

    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
        title: Image.asset('assets/background_icon.png', height: 40),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.chat_bubble_outline),
            onPressed: () {
              // TODO: Xử lý nút chat
            },
          ),
          IconButton(
            icon: const Icon(Icons.share),
            onPressed: () {
              // TODO: Xử lý nút chia sẻ
            },
          ),
          IconButton(
            icon: const Icon(Icons.favorite_border),
            onPressed: () {
              setState(() {
                likeCount += 1;
              });
            },
          ),
          IconButton(
            icon: Stack(
              children: [
                const Icon(Icons.shopping_cart),
                if (cart.isNotEmpty)
                  Positioned(
                    right: 0,
                    child: Container(
                      padding: const EdgeInsets.all(2),
                      decoration: BoxDecoration(
                        color: Theme.of(context).colorScheme.primary,
                        borderRadius: BorderRadius.circular(10),
                      ),
                      constraints: const BoxConstraints(
                        minWidth: 16,
                        minHeight: 16,
                      ),
                      child: Text(
                        '${cart.length}',
                        style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          color: Theme.of(context).colorScheme.onPrimary,
                          fontSize: 10,
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ),
                  ),
              ],
            ),
            onPressed: () => Navigator.pushNamed(context, '/cart'),
          ),
        ],
      ),
      body: Container(
        color: Theme.of(context).scaffoldBackgroundColor,
        child: Stack(
          children: [
            SingleChildScrollView(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Ảnh sản phẩm
                  Stack(
                    children: [
                      ClipRRect(
                        borderRadius: const BorderRadius.vertical(bottom: Radius.circular(12)),
                        child: Image.network(
                          product.imageUrl,
                          height: 350,
                          width: double.infinity,
                          fit: BoxFit.cover,
                          errorBuilder: (context, error, stackTrace) => const Icon(Icons.error, size: 100),
                        ),
                      ),
                      Positioned(
                        bottom: 10,
                        left: 0,
                        right: 0,
                        child: SizedBox(
                          height: 60,
                          child: ListView.builder(
                            scrollDirection: Axis.horizontal,
                            padding: const EdgeInsets.symmetric(horizontal: 16),
                            itemCount: imageUrls.length,
                            itemBuilder: (context, index) {
                              return Padding(
                                padding: const EdgeInsets.symmetric(horizontal: 4),
                                child: ClipRRect(
                                  borderRadius: BorderRadius.circular(8),
                                  child: Image.network(
                                    imageUrls[index],
                                    width: 60,
                                    height: 60,
                                    fit: BoxFit.cover,
                                    errorBuilder: (context, error, stackTrace) => const Icon(Icons.error),
                                  ),
                                ),
                              );
                            },
                          ),
                        ),
                      ),
                    ],
                  ),
                  // Thông tin người bán
                  Padding(
                    padding: const EdgeInsets.all(16),
                    child: Row(
                      children: [
                        CircleAvatar(
                          radius: 20,
                          backgroundImage: NetworkImage(product.seller.avatarUrl),
                          onBackgroundImageError: (_, __) => const Icon(Icons.error),
                        ),
                        const SizedBox(width: 12),
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              product.seller.name,
                              style: Theme.of(context).textTheme.titleLarge,
                            ),
                            Text(
                              'Đăng lúc: ${product.seller.postTime}',
                              style: Theme.of(context).textTheme.bodyMedium,
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                  // Thông tin sản phẩm
                  Container(
                    margin: const EdgeInsets.only(top: 0, bottom: 80),
                    padding: const EdgeInsets.all(16),
                    color: Theme.of(context).colorScheme.surface,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          product.name,
                          style: Theme.of(context).textTheme.headlineSmall,
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Text(
                              '${product.price.toStringAsFixed(0)} VNĐ',
                              style: Theme.of(context).textTheme.titleLarge?.copyWith(
                                color: Theme.of(context).colorScheme.primary,
                              ),
                            ),
                            const SizedBox(width: 16),
                            Text(
                              'Đã bán 1.2k',
                              style: Theme.of(context).textTheme.bodyMedium,
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Icon(Icons.star, color: Theme.of(context).colorScheme.primary, size: 18),
                            const SizedBox(width: 4),
                            Text(
                              '4.8 (500 đánh giá)',
                              style: Theme.of(context).textTheme.bodyMedium,
                            ),
                          ],
                        ),
                        const Divider(height: 24),
                        Text(
                          'Loại: ${product.category}',
                          style: Theme.of(context).textTheme.bodyLarge,
                        ),
                        const SizedBox(height: 8),
                        Text(
                          'Ngày nhập: ${product.importDate}',
                          style: Theme.of(context).textTheme.bodyLarge,
                        ),
                        const SizedBox(height: 8),
                        Text(
                          'Mô tả: ${product.description}',
                          style: Theme.of(context).textTheme.bodyLarge,
                        ),
                        const SizedBox(height: 8),
                        Text(
                          'Chi tiết: ${product.details}',
                          style: Theme.of(context).textTheme.bodyLarge,
                        ),
                      ],
                    ),
                  ),
                  // Phần bình luận
                  Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              'Bình luận (${product.comments.length})',
                              style: Theme.of(context).textTheme.titleLarge,
                            ),
                            Row(
                              children: [
                                IconButton(
                                  icon: Icon(
                                    Icons.favorite_border,
                                    color: Theme.of(context).colorScheme.primary,
                                  ),
                                  onPressed: () {
                                    setState(() {
                                      likeCount += 1;
                                    });
                                  },
                                ),
                                Text(
                                  '$likeCount lượt thích',
                                  style: Theme.of(context).textTheme.bodyMedium,
                                ),
                                IconButton(
                                  icon: Icon(
                                    Icons.comment,
                                    color: Theme.of(context).colorScheme.primary,
                                  ),
                                  onPressed: () {
                                    // TODO: Xử lý mở khu vực bình luận
                                  },
                                ),
                              ],
                            ),
                          ],
                        ),
                        SizedBox(
                          height: 400,
                          child: ListView.builder(
                            shrinkWrap: true,
                            itemCount: product.comments.length,
                            itemBuilder: (context, index) {
                              final comment = product.comments[index];
                              return Padding(
                                padding: const EdgeInsets.symmetric(vertical: 8),
                                child: Row(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    CircleAvatar(
                                      radius: 20,
                                      backgroundImage: NetworkImage(comment.avatarUrl),
                                      onBackgroundImageError: (_, __) => const Icon(Icons.error),
                                    ),
                                    const SizedBox(width: 12),
                                    Expanded(
                                      child: Column(
                                        crossAxisAlignment: CrossAxisAlignment.start,
                                        children: [
                                          Row(
                                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                            children: [
                                              Text(
                                                comment.name,
                                                style: Theme.of(context).textTheme.titleMedium,
                                              ),
                                              Text(
                                                comment.time,
                                                style: Theme.of(context).textTheme.bodyMedium,
                                              ),
                                            ],
                                          ),
                                          const SizedBox(height: 4),
                                          Text(
                                            comment.content,
                                            style: Theme.of(context).textTheme.bodyLarge,
                                          ),
                                          const SizedBox(height: 4),
                                          Row(
                                            children: [
                                              IconButton(
                                                icon: Icon(
                                                  comment.liked
                                                      ? Icons.favorite
                                                      : Icons.favorite_border,
                                                  color: Theme.of(context).colorScheme.primary,
                                                  size: 18,
                                                ),
                                                onPressed: () {
                                                  setState(() {
                                                    comment.liked = !comment.liked;
                                                    if (comment.liked) {
                                                      comment.likeCount += 1;
                                                    } else {
                                                      comment.likeCount -= 1;
                                                    }
                                                  });
                                                },
                                              ),
                                              Text(
                                                '${comment.likeCount} lượt thích',
                                                style: Theme.of(context).textTheme.bodyMedium,
                                              ),
                                            ],
                                          ),
                                        ],
                                      ),
                                    ),
                                  ],
                                ),
                              );
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            // Nút cố định ở dưới và ô nhập bình luận
            Positioned(
              bottom: 0,
              left: 0,
              right: 0,
              child: Container(
                padding: const EdgeInsets.all(16),
                color: Theme.of(context).colorScheme.surface,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        CircleAvatar(
                          radius: 20,
                          backgroundImage: NetworkImage(
                            'https://i.pravatar.cc/50?img=${product.id + 10}',
                          ),
                          onBackgroundImageError: (_, __) => const Icon(Icons.error),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: TextField(
                            controller: _commentController,
                            decoration: InputDecoration(
                              hintText: 'Viết bình luận...',
                              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                              border: Theme.of(context).inputDecorationTheme.border,
                              enabledBorder: Theme.of(context).inputDecorationTheme.enabledBorder,
                              focusedBorder: Theme.of(context).inputDecorationTheme.focusedBorder,
                              labelStyle: Theme.of(context).inputDecorationTheme.labelStyle,
                            ),
                          ),
                        ),
                        const SizedBox(width: 8),
                        ElevatedButton(
                          onPressed: () {
                            if (_commentController.text.isNotEmpty) {
                              setState(() {
                                product.comments.add(Comment(
                                  id: product.comments.length + 1,
                                  avatarUrl: 'https://i.pravatar.cc/50?img=${product.id + 10}',
                                  name: 'Bạn',
                                  content: _commentController.text,
                                  time: '2025-05-19 22:24',
                                  liked: false,
                                  likeCount: 0,
                                ));
                                _commentController.clear();
                              });
                            }
                          },
                          style: Theme.of(context).elevatedButtonTheme.style,
                          child: const Text('Gửi'),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        Expanded(
                          child: ElevatedButton(
                            onPressed: () {
                              ref.read(cartProvider.notifier).addToCart(product);
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(
                                  content: Text(
                                    '${product.name} đã được thêm vào giỏ',
                                    style: Theme.of(context).snackBarTheme.contentTextStyle,
                                  ),
                                  backgroundColor: Theme.of(context).snackBarTheme.backgroundColor,
                                  behavior: Theme.of(context).snackBarTheme.behavior,
                                  shape: Theme.of(context).snackBarTheme.shape,
                                  margin: const EdgeInsets.only(top: 50, left: 16, right: 16),
                                  elevation: Theme.of(context).snackBarTheme.elevation,
                                  duration: const Duration(seconds: 2),
                                ),
                              );
                            },
                            style: Theme.of(context).elevatedButtonTheme.style?.copyWith(
                              backgroundColor: WidgetStateProperty.all(Theme.of(context).colorScheme.surface),
                              foregroundColor: WidgetStateProperty.all(Theme.of(context).colorScheme.primary),
                              side: WidgetStateProperty.all(
                                BorderSide(color: Theme.of(context).colorScheme.primary, width: 1),
                              ),
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Icon(Icons.add_shopping_cart, size: 18, color: Theme.of(context).colorScheme.primary),
                                const SizedBox(width: 8),
                                Text(
                                  'Thêm vào giỏ',
                                  style: Theme.of(context).textTheme.labelLarge?.copyWith(
                                    color: Theme.of(context).colorScheme.primary,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: ElevatedButton(
                            onPressed: () {
                              ref.read(cartProvider.notifier).addToCart(product);
                              Navigator.pushNamed(context, '/cart');
                            },
                            style: Theme.of(context).elevatedButtonTheme.style,
                            child: const Text('Mua ngay'),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}