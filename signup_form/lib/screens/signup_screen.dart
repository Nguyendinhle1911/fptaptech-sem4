import 'package:flutter/material.dart';

class SignupScreen extends StatefulWidget {
  @override
  _SignupScreenState createState() => _SignupScreenState();
}

class _SignupScreenState extends State<SignupScreen> {
  final _formKey = GlobalKey<FormState>();
  String? _day;
  String? _month;
  String? _year;
  String? _gender;
  final TextEditingController _firstNameController = TextEditingController();
  final TextEditingController _lastNameController = TextEditingController();
  final TextEditingController _emailOrPhoneController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isEditing = false;

  final FocusNode _firstNameFocus = FocusNode();
  final FocusNode _lastNameFocus = FocusNode();
  final FocusNode _emailOrPhoneFocus = FocusNode();
  final FocusNode _passwordFocus = FocusNode();

  @override
  void initState() {
    super.initState();
    _firstNameFocus.addListener(_onFocusChange);
    _lastNameFocus.addListener(_onFocusChange);
    _emailOrPhoneFocus.addListener(_onFocusChange);
    _passwordFocus.addListener(_onFocusChange);
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _emailOrPhoneController.dispose();
    _passwordController.dispose();
    _firstNameFocus.dispose();
    _lastNameFocus.dispose();
    _emailOrPhoneFocus.dispose();
    _passwordFocus.dispose();
    super.dispose();
  }

  void _onFocusChange() {
    setState(() {});
  }

  void _submitForm() {
    if (_formKey.currentState!.validate()) {
      // Kiểm tra ngày sinh đầy đủ
      if (_day == null || _month == null || _year == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Vui lòng chọn ngày sinh đầy đủ')),
        );
        return;
      }

      // Kiểm tra giới tính
      if (_gender == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Vui lòng chọn giới tính')),
        );
        return;
      }

      // Kiểm tra ngày sinh hợp lệ
      DateTime selectedDate;
      try {
        selectedDate = DateTime(int.parse(_year!), int.parse(_month!), int.parse(_day!));
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Ngày sinh không hợp lệ')),
        );
        return;
      }

      // Kiểm tra ngày sinh trong tương lai
      DateTime currentDate = DateTime(2025, 5, 27, 23, 50); // Thời gian hiện tại
      if (selectedDate.isAfter(currentDate)) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Ngày sinh không được trong tương lai')),
        );
        return;
      }

      // Kiểm tra tuổi dưới 13
      int age = currentDate.year - selectedDate.year;
      if (currentDate.month < selectedDate.month ||
          (currentDate.month == selectedDate.month && currentDate.day < selectedDate.day)) {
        age--;
      }
      if (age < 13) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Bạn phải từ 13 tuổi trở lên')),
        );
        return;
      }

      // Nếu tất cả hợp lệ, hiển thị thông báo thành công
      setState(() {
        _isEditing = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Đăng ký thành công!')),
      );
    } else {
      setState(() {
        _isEditing = true;
      });
    }
  }

  void _editForm() {
    setState(() {
      _isEditing = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 20.0),
        child: Form(
          key: _formKey,
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Padding(
                  padding: EdgeInsets.only(top: 20.0, bottom: 20.0),
                  child: Text(
                    'facebook',
                    style: TextStyle(
                      fontSize: 40,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1877F2),
                    ),
                    key: ValueKey('logo'),
                  ),
                ),
                Text(
                  'Tạo tài khoản mới',
                  style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                  key: ValueKey('title'),
                ),
                SizedBox(height: 5),
                Text(
                  'Nhanh chóng và dễ dàng.',
                  style: TextStyle(fontSize: 16, color: Colors.grey[600]),
                  key: ValueKey('subtitle'),
                ),
                SizedBox(height: 20),
                Row(
                  children: [
                    Expanded(
                      child: TextFormField(
                        controller: _firstNameController,
                        focusNode: _firstNameFocus,
                        decoration: InputDecoration(
                          labelText: 'Họ',
                          border: OutlineInputBorder(),
                          contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                          errorBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black),
                          ),
                          enabledBorder: OutlineInputBorder(
                            borderSide: BorderSide(
                              color: _firstNameFocus.hasFocus ? Colors.black : Colors.grey,
                            ),
                          ),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Vui lòng nhập họ')),
                            );
                            return '';
                          }
                          if (RegExp(r'[!@#$%^&*(),.?":{}|<>]').hasMatch(value)) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Họ không được chứa ký tự đặc biệt')),
                            );
                            return '';
                          }
                          if (value.length > 50) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Họ không được vượt quá 50 ký tự')),
                            );
                            return '';
                          }
                          return null;
                        },
                        key: ValueKey('firstNameField'),
                      ),
                    ),
                    SizedBox(width: 10),
                    Expanded(
                      child: TextFormField(
                        controller: _lastNameController,
                        focusNode: _lastNameFocus,
                        decoration: InputDecoration(
                          labelText: 'Tên',
                          border: OutlineInputBorder(),
                          contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                          errorBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black),
                          ),
                          enabledBorder: OutlineInputBorder(
                            borderSide: BorderSide(
                              color: _lastNameFocus.hasFocus ? Colors.black : Colors.grey,
                            ),
                          ),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Vui lòng nhập tên')),
                            );
                            return '';
                          }
                          if (RegExp(r'[!@#$%^&*(),.?":{}|<>]').hasMatch(value)) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Tên không được chứa ký tự đặc biệt')),
                            );
                            return '';
                          }
                          if (value.length > 50) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Tên không được vượt quá 50 ký tự')),
                            );
                            return '';
                          }
                          return null;
                        },
                        key: ValueKey('lastNameField'),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 20),
                Row(
                  children: [
                    Expanded(
                      child: DropdownButtonFormField<String>(
                        decoration: InputDecoration(
                          labelText: 'Ngày sinh',
                          border: OutlineInputBorder(),
                          contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                          errorBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black),
                          ),
                        ),
                        items: List.generate(31, (index) => (index + 1).toString())
                            .map((e) => DropdownMenuItem(value: e, child: Text(e)))
                            .toList(),
                        onChanged: (value) {
                          setState(() {
                            _day = value;
                          });
                        },
                        value: _day,
                        validator: (value) => value == null ? '' : null,
                        key: ValueKey('dayField'),
                      ),
                    ),
                    SizedBox(width: 10),
                    Expanded(
                      child: DropdownButtonFormField<String>(
                        decoration: InputDecoration(
                          labelText: 'Tháng',
                          border: OutlineInputBorder(),
                          contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                          errorBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black),
                          ),
                        ),
                        items: List.generate(12, (index) => (index + 1).toString())
                            .map((e) => DropdownMenuItem(value: e, child: Text(e)))
                            .toList(),
                        onChanged: (value) {
                          setState(() {
                            _month = value;
                          });
                        },
                        value: _month,
                        validator: (value) => value == null ? '' : null,
                        key: ValueKey('monthField'),
                      ),
                    ),
                    SizedBox(width: 10),
                    Expanded(
                      child: DropdownButtonFormField<String>(
                        decoration: InputDecoration(
                          labelText: 'Năm',
                          border: OutlineInputBorder(),
                          contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                          errorBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.red),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black),
                          ),
                        ),
                        items: List.generate(100, (index) => (2025 - index).toString())
                            .map((e) => DropdownMenuItem(value: e, child: Text(e)))
                            .toList(),
                        onChanged: (value) {
                          setState(() {
                            _year = value;
                          });
                        },
                        value: _year,
                        validator: (value) => value == null ? '' : null,
                        key: ValueKey('yearField'),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 20),
                Text('Giới tính', style: TextStyle(fontSize: 16)),
                Row(
                  children: [
                    Expanded(
                      child: Row(
                        children: [
                          Radio<String>(
                            value: 'Nữ',
                            groupValue: _gender,
                            onChanged: (value) {
                              setState(() {
                                _gender = value;
                              });
                            },
                            key: ValueKey('femaleRadio'),
                          ),
                          Text('Nữ'),
                        ],
                      ),
                    ),
                    Expanded(
                      child: Row(
                        children: [
                          Radio<String>(
                            value: 'Nam',
                            groupValue: _gender,
                            onChanged: (value) {
                              setState(() {
                                _gender = value;
                              });
                            },
                            key: ValueKey('maleRadio'),
                          ),
                          Text('Nam'),
                        ],
                      ),
                    ),
                    Expanded(
                      child: Row(
                        children: [
                          Radio<String>(
                            value: 'Tùy chỉnh',
                            groupValue: _gender,
                            onChanged: (value) {
                              setState(() {
                                _gender = value;
                              });
                            },
                            key: ValueKey('customRadio'),
                          ),
                          Text('Tùy chỉnh'),
                        ],
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 20),
                TextFormField(
                  controller: _emailOrPhoneController,
                  focusNode: _emailOrPhoneFocus,
                  decoration: InputDecoration(
                    labelText: 'Số điện thoại hoặc email',
                    border: OutlineInputBorder(),
                    contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                    errorBorder: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.red),
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.black),
                    ),
                    enabledBorder: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: _emailOrPhoneFocus.hasFocus ? Colors.black : Colors.grey,
                      ),
                    ),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Vui lòng nhập số điện thoại hoặc email')),
                      );
                      return '';
                    }
                    if (value.length > 100) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Email/SĐT không được vượt quá 100 ký tự')),
                      );
                      return '';
                    }
                    if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(value) &&
                        !RegExp(r'^\d{10}$').hasMatch(value)) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Số điện thoại hoặc email không hợp lệ')),
                      );
                      return '';
                    }
                    return null;
                  },
                  key: ValueKey('emailOrPhoneField'),
                ),
                SizedBox(height: 20),
                TextFormField(
                  controller: _passwordController,
                  obscureText: true,
                  focusNode: _passwordFocus,
                  decoration: InputDecoration(
                    labelText: 'Mật khẩu mới',
                    border: OutlineInputBorder(),
                    contentPadding: EdgeInsets.symmetric(horizontal: 10, vertical: 15),
                    errorBorder: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.red),
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.black),
                    ),
                    enabledBorder: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: _passwordFocus.hasFocus ? Colors.black : Colors.grey,
                      ),
                    ),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Vui lòng nhập mật khẩu')),
                      );
                      return '';
                    }
                    if (value.length < 6) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Mật khẩu phải có ít nhất 6 ký tự')),
                      );
                      return '';
                    }
                    if (value.length > 50) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Mật khẩu không được vượt quá 50 ký tự')),
                      );
                      return '';
                    }
                    if (value.contains(' ')) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Mật khẩu không được chứa khoảng trắng')),
                      );
                      return '';
                    }
                    return null;
                  },
                  key: ValueKey('passwordField'),
                ),
                SizedBox(height: 20),
                Text(
                  'Những người dùng dịch vụ của chúng tôi có thể đã tải thông tin liên hệ của bạn lên Facebook. Tìm hiểu thêm.\n\nBằng cách nhấn vào Đăng ký, bạn đồng ý với Điều khoản, Chính sách quyền riêng tư và Chính sách cookie của chúng tôi. Bạn có thể nhận được thông báo của chúng tôi qua SMS và hủy nhận bất kỳ lúc nào.',
                  style: TextStyle(fontSize: 12, color: Colors.grey[600]),
                  key: ValueKey('termsText'),
                ),
                SizedBox(height: 20),
                if (_isEditing)
                  ElevatedButton(
                    onPressed: _editForm,
                    child: Text(
                      'Quay lại/Chỉnh sửa',
                      style: TextStyle(fontSize: 14, color: Colors.white),
                    ),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey[600],
                      minimumSize: Size(double.infinity, 50),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(5),
                      ),
                    ),
                    key: ValueKey('editButton'),
                  ),
                if (_isEditing) SizedBox(height: 10),
                ElevatedButton(
                  onPressed: _submitForm,
                  child: Text(
                    'Đăng ký',
                    style: TextStyle(fontSize: 16, color: Colors.white),
                  ),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Color(0xFF00A400),
                    minimumSize: Size(double.infinity, 50),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  key: ValueKey('signupButton'),
                ),
                SizedBox(height: 10),
                Center(
                  child: Text(
                    'Bạn đã có tài khoản?',
                    style: TextStyle(color: Color(0xFF1877F2), fontSize: 14),
                    key: ValueKey('alreadyHaveAccount'),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}