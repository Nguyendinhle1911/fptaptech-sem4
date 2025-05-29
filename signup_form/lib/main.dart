import 'package:flutter/material.dart';
import 'package:flutter_driver/driver_extension.dart';
import 'screens/signup_screen.dart';

void main() {
  // Kích hoạt Flutter Driver extension để hỗ trợ kiểm thử tự động
  enableFlutterDriverExtension();

  // Chạy ứng dụng
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Form Đăng ký Người dùng',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: SignupScreen(), // Sử dụng màn hình đăng ký
    );
  }
}