// Model cho công việc
class Task {
  String title;
  bool isCompleted;

  Task({required this.title, this.isCompleted = false});

  // Chuyển Task thành JSON để lưu trữ
  Map<String, dynamic> toJson() => {
    'title': title,
    'isCompleted': isCompleted,
  };

  // Tạo Task từ JSON
  factory Task.fromJson(Map<String, dynamic> json) => Task(
    title: json['title'] as String,
    isCompleted: json['isCompleted'] as bool,
  );
}