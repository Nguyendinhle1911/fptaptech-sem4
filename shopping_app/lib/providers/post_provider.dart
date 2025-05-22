import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/post.dart';
import 'package:intl/intl.dart';

class PostsNotifier extends StateNotifier<List<Post>> {
  PostsNotifier() : super(_initialPosts);

  static final List<Post> _initialPosts = [
    Post(
      id: '1',
      author: 'Luxury Store',
      content: 'New Gucci bag available!',
      price: '25,000,000 VNĐ',
      time: DateFormat('dd/MM/yyyy HH:mm').format(DateTime.now()),
      location: 'Hanoi',
      images: ['https://via.placeholder.com/300'],
      isLiked: false,
      isFollowing: false,
      likes: 10,
      comments: [],
    ),
  ];

  void addPost(String content, String price, List<String> images) {
    final newPost = Post(
      id: DateTime.now().millisecondsSinceEpoch.toString(),
      author: 'Current User',
      content: content,
      price: price.isEmpty ? 'Liên hệ' : '$price VNĐ',
      time: DateFormat('dd/MM/yyyy HH:mm').format(DateTime.now()),
      location: 'Unknown',
      images: images,
      isLiked: false,
      isFollowing: false,
      likes: 0,
      comments: [],
    );
    state = [...state, newPost];
  }

  void toggleLike(String postId) {
    state = state.map((post) {
      if (post.id == postId) {
        return post.copyWith(
          isLiked: !post.isLiked,
          likes: post.isLiked ? post.likes - 1 : post.likes + 1,
        );
      }
      return post;
    }).toList();
  }

  void toggleFollow(String postId) {
    state = state.map((post) {
      if (post.id == postId) {
        return post.copyWith(isFollowing: !post.isFollowing);
      }
      return post;
    }).toList();
  }

  void addComment(String postId, Comment comment) {
    state = state.map((post) {
      if (post.id == postId) {
        return post.copyWith(
          comments: [...post.comments, comment],
        );
      }
      return post;
    }).toList();
  }
}

final postsProvider = StateNotifierProvider<PostsNotifier, List<Post>>((ref) {
  return PostsNotifier();
});