import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'task.dart';
import 'task_manager.dart';

class TaskScreen extends StatefulWidget {
  const TaskScreen({super.key});

  @override
  _TaskScreenState createState() => _TaskScreenState();
}

class _TaskScreenState extends State<TaskScreen> {
  final TaskManager _taskManager = TaskManager();
  final _taskController = TextEditingController();
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    // Tải danh sách công việc khi khởi động
    _taskManager.loadTasks().then((_) {
      setState(() {
        _isLoading = false;
      });
    });
  }

  // Thêm công việc mới
  void _addTask() {
    final title = _taskController.text.trim();
    if (title.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Vui lòng nhập công việc')),
      );
      return;
    }
    setState(() {
      _taskManager.addTask(title);
    });
    _taskController.clear();
  }

  // Chỉnh sửa công việc
  void _editTask(int index) {
    final editController = TextEditingController(text: _taskManager.tasks[index].title);
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Chỉnh sửa công việc'),
        content: TextField(
          controller: editController,
          decoration: const InputDecoration(
            labelText: 'Tên công việc',
            border: OutlineInputBorder(),
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Hủy'),
          ),
          TextButton(
            onPressed: () {
              final newTitle = editController.text.trim();
              if (newTitle.isNotEmpty) {
                setState(() {
                  _taskManager.editTask(index, newTitle);
                });
              }
              Navigator.pop(context);
            },
            child: const Text('Lưu'),
          ),
        ],
      ),
    );
  }

  // Xác nhận xóa công việc
  void _confirmDeleteTask(int index) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Xóa công việc'),
        content: const Text('Bạn có chắc muốn xóa công việc này?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Hủy'),
          ),
          TextButton(
            onPressed: () {
              setState(() {
                _taskManager.deleteTask(index);
              });
              Navigator.pop(context);
            },
            child: const Text('Xóa'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('To-Do List'),
        flexibleSpace: Container(
          decoration: const BoxDecoration(
            gradient: LinearGradient(
              colors: [Colors.blue, Colors.purple],
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
            ),
          ),
        ),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // Form thêm công việc
            Container(
              padding: const EdgeInsets.all(8.0),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: Colors.grey.withOpacity(0.3),
                    spreadRadius: 2,
                    blurRadius: 5,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _taskController,
                      decoration: InputDecoration(
                        labelText: 'Công việc mới',
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                        prefixIcon: const Icon(CupertinoIcons.doc_text),
                      ),
                      onSubmitted: (_) => _addTask(),
                    ),
                  ),
                  const SizedBox(width: 8),
                  ElevatedButton.icon(
                    onPressed: _addTask,
                    icon: const Icon(CupertinoIcons.add),
                    label: const Text('Thêm'),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blue,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            // Danh sách công việc
            Expanded(
              child: _taskManager.tasks.isEmpty
                  ? const Center(
                child: Text(
                  'Chưa có công việc nào',
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                ),
              )
                  : ListView.builder(
                itemCount: _taskManager.tasks.length,
                itemBuilder: (context, index) {
                  final task = _taskManager.tasks[index];
                  return Animate(
                    effects: const [
                      FadeEffect(duration: Duration(milliseconds: 300)),
                      ScaleEffect(
                          begin: Offset(0.9, 0.9),
                          end: Offset(1.0, 1.0),
                          duration: Duration(milliseconds: 300)),
                    ],
                    child: Card(
                      elevation: 3,
                      margin: const EdgeInsets.symmetric(vertical: 4),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: ListTile(
                        leading: Checkbox(
                          value: task.isCompleted,
                          onChanged: (value) {
                            setState(() {
                              _taskManager.toggleTaskCompletion(index);
                            });
                          },
                          activeColor: Colors.green,
                        ),
                        title: Text(
                          task.title,
                          style: TextStyle(
                            decoration: task.isCompleted
                                ? TextDecoration.lineThrough
                                : null,
                            color: task.isCompleted ? Colors.grey : Colors.black,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: const Icon(CupertinoIcons.pencil, color: Colors.blue),
                              onPressed: () => _editTask(index),
                            ),
                            IconButton(
                              icon: const Icon(CupertinoIcons.delete, color: Colors.red),
                              onPressed: () => _confirmDeleteTask(index),
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}