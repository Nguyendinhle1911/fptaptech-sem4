import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'task.dart';

// Quản lý danh sách công việc
class TaskManager {
  List<Task> tasks = [];
  static const String _tasksKey = 'tasks';

  // Tải danh sách công việc từ SharedPreferences
  Future<void> loadTasks() async {
    final prefs = await SharedPreferences.getInstance();
    final tasksJson = prefs.getString(_tasksKey);
    if (tasksJson != null) {
      final List<dynamic> decoded = jsonDecode(tasksJson);
      tasks = decoded.map((json) => Task.fromJson(json)).toList();
    }
  }

  // Lưu danh sách công việc vào SharedPreferences
  Future<void> saveTasks() async {
    final prefs = await SharedPreferences.getInstance();
    final tasksJson = jsonEncode(tasks.map((task) => task.toJson()).toList());
    await prefs.setString(_tasksKey, tasksJson);
  }

  // Thêm công việc mới
  void addTask(String title) {
    tasks.add(Task(title: title));
    saveTasks();
  }

  // Đánh dấu hoàn thành công việc
  void toggleTaskCompletion(int index) {
    tasks[index].isCompleted = !tasks[index].isCompleted;
    saveTasks();
  }

  // Xóa công việc
  void deleteTask(int index) {
    tasks.removeAt(index);
    saveTasks();
  }

  // Chỉnh sửa tiêu đề công việc
  void editTask(int index, String newTitle) {
    tasks[index].title = newTitle;
    saveTasks();
  }
}