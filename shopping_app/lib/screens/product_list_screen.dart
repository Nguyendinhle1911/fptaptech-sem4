import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:carousel_slider/carousel_slider.dart' as carousel;
import '../models/product.dart';
import '../providers/cart_provider.dart';

class ProductListScreen extends ConsumerStatefulWidget {
  const ProductListScreen({super.key});

  @override
  ConsumerState<ProductListScreen> createState() => _ProductListScreenState();
}

class _ProductListScreenState extends ConsumerState<ProductListScreen> {
  List<Product> products = [];
  List<Product> filteredProducts = [];
  bool isLoading = true;
  TextEditingController searchController = TextEditingController();
  int _currentBannerIndex = 0;
  final FocusNode _searchFocusNode = FocusNode();

  final List<String> bannerImages = [
    'https://biti.vn/wp-content/uploads/2022/03/banner-web-Khuyen-mai-mua-hang-online-75-1024x745.jpg',
    'https://bizweb.dktcdn.net/100/438/408/themes/915505/assets/slider_2.jpg?1711158638418',
    'https://bizweb.dktcdn.net/100/438/408/themes/915505/assets/slider_3.jpg?1711158638418',
  ];

  @override
  void initState() {
    super.initState();
    loadProducts();
    _searchFocusNode.addListener(() {
      setState(() {});
    });
  }

  Future<void> loadProducts() async {
    try {
      final String response = await DefaultAssetBundle.of(context).loadString('assets/products.json');
      final List<dynamic> data = jsonDecode(response);
      setState(() {
        products = data.map((json) => Product.fromJson(json)).toList();
        filteredProducts = products;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            'Lỗi tải dữ liệu: $e',
            style: Theme.of(context).snackBarTheme.contentTextStyle,
          ),
          backgroundColor: Theme.of(context).snackBarTheme.backgroundColor,
          behavior: Theme.of(context).snackBarTheme.behavior,
          shape: Theme.of(context).snackBarTheme.shape,
          margin: const EdgeInsets.only(top: 50, left: 16, right: 16),
          elevation: Theme.of(context).snackBarTheme.elevation,
          duration: const Duration(seconds: 3),
        ),
      );
    }
  }

  void filterProducts(String query) {
    setState(() {
      filteredProducts = products
          .where((product) => product.name.toLowerCase().contains(query.toLowerCase()))
          .toList();
    });
  }

  @override
  void dispose() {
    searchController.dispose();
    _searchFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Theme.of(context).scaffoldBackgroundColor,
      child: isLoading
          ? Center(
        child: SpinKitFadingCircle(
          color: Theme.of(context).colorScheme.primary,
          size: 50.0,
        ),
      )
          : Column(
        children: [
          // Slider banner
          Stack(
            children: [
              carousel.CarouselSlider(
                options: carousel.CarouselOptions(
                  height: 180,
                  autoPlay: true,
                  autoPlayInterval: const Duration(seconds: 3),
                  enlargeCenterPage: true,
                  viewportFraction: 1.0,
                  onPageChanged: (index, reason) {
                    setState(() {
                      _currentBannerIndex = index;
                    });
                  },
                ),
                items: bannerImages.map((imageUrl) {
                  return Builder(
                    builder: (BuildContext context) {
                      return Container(
                        width: MediaQuery.of(context).size.width,
                        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(16),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.2),
                              blurRadius: 6,
                              offset: const Offset(0, 3),
                            ),
                          ],
                        ),
                        child: ClipRRect(
                          borderRadius: BorderRadius.circular(16),
                          child: Stack(
                            fit: StackFit.expand,
                            children: [
                              Image.network(
                                imageUrl,
                                fit: BoxFit.cover,
                                errorBuilder: (context, error, stackTrace) => Container(
                                  color: Colors.grey.shade300,
                                  child: const Center(child: Icon(Icons.error)),
                                ),
                              ),
                              Container(
                                decoration: BoxDecoration(
                                  gradient: LinearGradient(
                                    begin: Alignment.topCenter,
                                    end: Alignment.bottomCenter,
                                    colors: [
                                      Colors.black.withOpacity(0.4),
                                      Colors.black.withOpacity(0.1),
                                    ],
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  );
                }).toList(),
              ),
              Positioned(
                bottom: 16,
                left: 0,
                right: 0,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: bannerImages.asMap().entries.map((entry) {
                    return Container(
                      width: 8.0,
                      height: 8.0,
                      margin: const EdgeInsets.symmetric(horizontal: 4.0),
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: _currentBannerIndex == entry.key
                            ? Theme.of(context).colorScheme.primary
                            : Colors.white.withOpacity(0.5),
                      ),
                    );
                  }).toList(),
                ),
              ),
            ],
          ),
          // Thanh tìm kiếm
          Container(
            padding: const EdgeInsets.only(left: 16, right: 16, bottom: 8, top: 12),
            child: AnimatedContainer(
              duration: const Duration(milliseconds: 200),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.surface,
                borderRadius: BorderRadius.circular(8),
                border: Border.all(
                  color: _searchFocusNode.hasFocus
                      ? Theme.of(context).colorScheme.primary
                      : Colors.grey,
                  width: 1,
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(_searchFocusNode.hasFocus ? 0.2 : 0.1),
                    blurRadius: _searchFocusNode.hasFocus ? 8 : 4,
                    spreadRadius: _searchFocusNode.hasFocus ? 1 : 0,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: TextField(
                controller: searchController,
                focusNode: _searchFocusNode,
                decoration: InputDecoration(
                  hintText: 'Tìm kiếm sản phẩm...',
                  hintStyle: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: Colors.grey.shade400,
                    fontStyle: FontStyle.italic,
                  ),
                  prefixIcon: Icon(
                    Icons.search,
                    color: Theme.of(context).colorScheme.primary,
                    size: 24,
                  ),
                  border: Theme.of(context).inputDecorationTheme.border,
                  enabledBorder: Theme.of(context).inputDecorationTheme.enabledBorder,
                  focusedBorder: Theme.of(context).inputDecorationTheme.focusedBorder,
                  contentPadding: Theme.of(context).inputDecorationTheme.contentPadding,
                ),
                onChanged: (value) => filterProducts(value),
              ),
            ),
          ),
          // Danh sách sản phẩm
          Expanded(
            child: GridView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 16,
                mainAxisSpacing: 16,
                childAspectRatio: 0.65,
              ),
              itemCount: filteredProducts.length,
              itemBuilder: (context, index) {
                final product = filteredProducts[index];
                return InkWell(
                  onTap: () {
                    Navigator.pushNamed(
                      context,
                      '/product_detail',
                      arguments: product,
                    );
                  },
                  borderRadius: BorderRadius.circular(8),
                  child: Card(
                    elevation: Theme.of(context).cardTheme.elevation,
                    shape: Theme.of(context).cardTheme.shape,
                    margin: Theme.of(context).cardTheme.margin,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: ClipRRect(
                            borderRadius: const BorderRadius.vertical(top: Radius.circular(8)),
                            child: Image.network(
                              product.imageUrl,
                              fit: BoxFit.cover,
                              width: double.infinity,
                              errorBuilder: (context, error, stackTrace) => const Icon(Icons.error),
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.all(8),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                product.name,
                                style: Theme.of(context).textTheme.titleMedium,
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                              ),
                              const SizedBox(height: 4),
                              Row(
                                children: [
                                  Text(
                                    '${product.price.toStringAsFixed(0)}',
                                    style: Theme.of(context).textTheme.titleLarge?.copyWith(
                                      color: Theme.of(context).colorScheme.primary,
                                    ),
                                  ),
                                  const SizedBox(width: 4),
                                  Text(
                                    'VNĐ',
                                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                                      color: Theme.of(context).colorScheme.primary,
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 4),
                              Text(
                                'Đã bán ${index * 100 + 50}',
                                style: Theme.of(context).textTheme.bodyMedium,
                              ),
                              const SizedBox(height: 8),
                              SizedBox(
                                width: double.infinity,
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
                                  style: Theme.of(context).elevatedButtonTheme.style,
                                  child: const Text('Thêm vào giỏ'),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}