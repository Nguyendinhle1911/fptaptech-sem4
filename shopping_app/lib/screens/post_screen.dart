import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/post.dart';
import '../providers/post_provider.dart';
import 'post_detail_screen.dart';
import 'create_post_screen.dart';

class PostScreen extends ConsumerWidget {
  const PostScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final posts = ref.watch(postsProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Hàng hiệu cao cấp'),
        backgroundColor: Colors.black,
      ),
      backgroundColor: Colors.white,
      body: posts.isEmpty
          ? const Center(child: Text('Chưa có bài viết nào!'))
          : ListView.separated(
        itemCount: posts.length,
        separatorBuilder: (context, index) => const SizedBox(height: 8),
        itemBuilder: (context, index) {
          final post = posts[index];
          return Card(
            elevation: 1,
            margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            child: Padding(
              padding: const EdgeInsets.all(14),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Header
                  Row(
                    children: [
                      const CircleAvatar(
                        backgroundColor: Colors.black,
                        child: Icon(Icons.store, color: Colors.white),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                Expanded(
                                  child: Text(
                                    post.author,
                                    style: const TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 15,
                                      color: Colors.black,
                                    ),
                                  ),
                                ),
                                TextButton(
                                  style: TextButton.styleFrom(
                                    backgroundColor: post.isFollowing
                                        ? Colors.orange.shade600
                                        : Colors.grey.shade300,
                                    foregroundColor: Colors.white,
                                    minimumSize: const Size(90, 30),
                                    padding: const EdgeInsets.symmetric(horizontal: 12),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(6),
                                    ),
                                  ),
                                  onPressed: () => ref.read(postsProvider.notifier).toggleFollow(post.id),
                                  child: Text(
                                    post.isFollowing ? 'Đang theo dõi' : '+ Theo dõi',
                                    style: const TextStyle(fontSize: 12),
                                  ),
                                ),
                              ],
                            ),
                            Text(
                              '${post.time} • ${post.location}',
                              style: const TextStyle(
                                fontSize: 11,
                                color: Colors.grey,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),

                  const SizedBox(height: 12),

                  // Nội dung mô tả
                  if (post.content.isNotEmpty)
                    Padding(
                      padding: const EdgeInsets.only(bottom: 6),
                      child: Text(
                        post.content,
                        style: const TextStyle(fontSize: 14, color: Colors.black87),
                      ),
                    ),

                  // Giá
                  if (post.price.isNotEmpty)
                    Container(
                      margin: const EdgeInsets.only(bottom: 8),
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                      decoration: BoxDecoration(
                        color: Colors.black,
                        borderRadius: BorderRadius.circular(6),
                      ),
                      child: Text(
                        post.price,
                        style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w500),
                      ),
                    ),

                  // Ảnh
                  if (post.images.isNotEmpty)
                    GestureDetector(
                      onTap: () => Navigator.push(
                        context,
                        MaterialPageRoute(builder: (_) => PostDetailScreen(postId: post.id)),
                      ),
                      child: SizedBox(
                        height: 200,
                        child: PageView.builder(
                          itemCount: post.images.length,
                          itemBuilder: (_, i) => Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 4),
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(8),
                              child: Image.network(
                                post.images[i],
                                fit: BoxFit.cover,
                                width: double.infinity,
                                loadingBuilder: (context, child, progress) =>
                                progress == null ? child : const Center(child: CircularProgressIndicator()),
                                errorBuilder: (_, __, ___) => Container(
                                  color: Colors.grey.shade300,
                                  alignment: Alignment.center,
                                  child: const Icon(Icons.broken_image, size: 40),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),

                  const SizedBox(height: 10),

                  // Tương tác
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      TextButton.icon(
                        onPressed: () => ref.read(postsProvider.notifier).toggleLike(post.id),
                        icon: Icon(
                          post.isLiked ? Icons.favorite : Icons.favorite_border,
                          color: post.isLiked ? Colors.red : Colors.black,
                          size: 20,
                        ),
                        label: Text(
                          '${post.likes}',
                          style: const TextStyle(color: Colors.black),
                        ),
                      ),
                      InkWell(
                        onTap: () => Navigator.push(
                          context,
                          MaterialPageRoute(builder: (_) => PostDetailScreen(postId: post.id)),
                        ),
                        child: Row(
                          children: [
                            const Icon(Icons.comment_outlined, size: 20, color: Colors.black),
                            const SizedBox(width: 4),
                            Text(
                              '${post.comments.length} Bình luận',
                              style: const TextStyle(color: Colors.black),
                            ),
                          ],
                        ),
                      ),
                      const Icon(Icons.star_border, color: Colors.amber, size: 22),
                    ],
                  ),
                ],
              ),
            ),
          );
        },
      ),

      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.orange,
        foregroundColor: Colors.white,
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const CreatePostScreen()),
          );
        },
        child: const Icon(Icons.add),
        tooltip: 'Đăng bài mới',
      ),
    );
  }
}