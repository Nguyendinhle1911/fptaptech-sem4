class Product {
  final int id;
  final String name;
  final String description;
  final String details;
  final double price;
  final String category;
  final String importDate;
  final String imageUrl;
  final Seller seller;
  final List<Comment> comments;

  Product({
    required this.id,
    required this.name,
    required this.description,
    required this.details,
    required this.price,
    required this.category,
    required this.importDate,
    required this.imageUrl,
    required this.seller,
    required this.comments,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      details: json['details'],
      price: json['price'].toDouble(),
      category: json['category'],
      importDate: json['importDate'],
      imageUrl: json['imageUrl'],
      seller: Seller.fromJson(json['seller']),
      comments: (json['comments'] as List)
          .map((commentJson) => Comment.fromJson(commentJson))
          .toList(),
    );
  }
}

class Seller {
  final String avatarUrl;
  final String name;
  final String postTime;

  Seller({
    required this.avatarUrl,
    required this.name,
    required this.postTime,
  });

  factory Seller.fromJson(Map<String, dynamic> json) {
    return Seller(
      avatarUrl: json['avatarUrl'],
      name: json['name'],
      postTime: json['postTime'],
    );
  }
}

class Comment {
  final int id;
  final String avatarUrl;
  final String name;
  final String content;
  final String time;
  bool liked;
  late final int likeCount;

  Comment({
    required this.id,
    required this.avatarUrl,
    required this.name,
    required this.content,
    required this.time,
    required this.liked,
    required this.likeCount,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'],
      avatarUrl: json['avatarUrl'],
      name: json['name'],
      content: json['content'],
      time: json['time'],
      liked: json['liked'] ?? false,
      likeCount: json['likeCount'],
    );
  }
}