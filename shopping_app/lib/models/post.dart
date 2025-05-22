class Comment {
  final String user;
  final String message;
  final String time;

  Comment({
    required this.user,
    required this.message,
    required this.time,
  });

  factory Comment.fromMap(Map<String, dynamic> map) {
    return Comment(
      user: map['user'] as String,
      message: map['message'] as String,
      time: map['time'] as String,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'user': user,
      'message': message,
      'time': time,
    };
  }
}

class Post {
  final String id;
  final String author;
  final String content;
  final String price;
  final String time;
  final String location;
  final List<String> images;
  final bool isLiked;
  final bool isFollowing;
  final int likes;
  final List<Comment> comments;

  Post({
    required this.id,
    required this.author,
    required this.content,
    required this.price,
    required this.time,
    required this.location,
    required this.images,
    required this.isLiked,
    required this.isFollowing,
    required this.likes,
    required this.comments,
  });

  Post copyWith({
    String? id,
    String? author,
    String? content,
    String? price,
    String? time,
    String? location,
    List<String>? images,
    bool? isLiked,
    bool? isFollowing,
    int? likes,
    List<Comment>? comments,
  }) {
    return Post(
      id: id ?? this.id,
      author: author ?? this.author,
      content: content ?? this.content,
      price: price ?? this.price,
      time: time ?? this.time,
      location: location ?? this.location,
      images: images ?? this.images,
      isLiked: isLiked ?? this.isLiked,
      isFollowing: isFollowing ?? this.isFollowing,
      likes: likes ?? this.likes,
      comments: comments ?? this.comments,
    );
  }

  factory Post.fromMap(Map<String, dynamic> map) {
    return Post(
      id: map['id'] as String,
      author: map['author'] as String,
      content: map['content'] as String,
      price: map['price'] as String,
      time: map['time'] as String,
      location: map['location'] as String,
      images: List<String>.from(map['images'] as List),
      isLiked: map['isLiked'] as bool,
      isFollowing: map['isFollowing'] as bool,
      likes: map['likes'] as int,
      comments: (map['comments'] as List)
          .map((comment) => Comment.fromMap(comment as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'author': author,
      'content': content,
      'price': price,
      'time': time,
      'location': location,
      'images': images,
      'isLiked': isLiked,
      'isFollowing': isFollowing,
      'likes': likes,
      'comments': comments.map((comment) => comment.toMap()).toList(),
    };
  }
}