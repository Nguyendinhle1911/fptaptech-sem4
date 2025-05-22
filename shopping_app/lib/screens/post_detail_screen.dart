import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/post.dart';
import '../providers/post_provider.dart';
import 'package:intl/intl.dart';

class PostDetailScreen extends ConsumerWidget {
  final String postId;

  const PostDetailScreen({super.key, required this.postId});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final posts = ref.watch(postsProvider);
    final controller = TextEditingController();

    final post = posts.firstWhere(
          (p) => p.id == postId,
      orElse: () => Post(
        id: '',
        author: 'Không tìm thấy',
        content: 'Bài viết không tồn tại',
        price: '',
        time: '',
        location: '',
        images: [],
        isLiked: false,
        isFollowing: false,
        likes: 0,
        comments: [],
      ),
    );

    if (post.id.isEmpty) {
      return Scaffold(
        appBar: AppBar(title: const Text('Lỗi')),
        body: const Center(child: Text('Không tìm thấy bài viết')),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text('Bài viết của ${post.author}')),
      body: Column(
        children: [
          Expanded(
            child: ListView(
              padding: const EdgeInsets.all(12),
              children: [
                Text(
                  post.content,
                  style: const TextStyle(fontSize: 16, color: Colors.black87),
                ),
                const SizedBox(height: 10),
                if (post.price.isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.only(bottom: 8),
                    child: Text(
                      'Giá: ${post.price}',
                      style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
                    ),
                  ),
                if (post.images.isNotEmpty)
                  SizedBox(
                    height: 200,
                    child: PageView(
                      children: post.images.map((img) => Image.network(img, fit: BoxFit.cover)).toList(),
                    ),
                  ),
                const SizedBox(height: 10),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text('${post.likes} Thích', style: const TextStyle(fontSize: 14)),
                    Text('${post.comments.length} Bình luận', style: const TextStyle(fontSize: 14)),
                  ],
                ),
                const Divider(),
                if (post.comments.isEmpty)
                  const Center(child: Text('Chưa có bình luận nào')),
                for (var c in post.comments)
                  ListTile(
                    leading: const CircleAvatar(child: Icon(Icons.person)),
                    title: Text(c.user, style: const TextStyle(fontWeight: FontWeight.bold)),
                    subtitle: Text(c.message),
                    trailing: Text(c.time, style: const TextStyle(fontSize: 10, color: Colors.grey)),
                  ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: controller,
                    decoration: const InputDecoration(
                      hintText: 'Nhập bình luận...',
                      border: OutlineInputBorder(),
                    ),
                    onSubmitted: (value) {
                      if (value.isNotEmpty) {
                        final comment = Comment(
                          user: 'Bạn',
                          message: value,
                          time: DateFormat('dd/MM/yyyy HH:mm').format(DateTime.now()),
                        );
                        ref.read(postsProvider.notifier).addComment(post.id, comment);
                        controller.clear();
                      }
                    },
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.send, color: Colors.orange),
                  onPressed: () {
                    if (controller.text.isNotEmpty) {
                      final comment = Comment(
                        user: 'Bạn',
                        message: controller.text,
                        time: DateFormat('dd/MM/yyyy HH:mm').format(DateTime.now()),
                      );
                      ref.read(postsProvider.notifier).addComment(post.id, comment);
                      controller.clear();
                    }
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}