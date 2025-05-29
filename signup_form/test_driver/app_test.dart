import 'package:flutter_driver/flutter_driver.dart';
import 'package:test/test.dart';

void main() {
  group('Signup Form Automation Tests', () {
    late FlutterDriver driver;

    // Thiết lập trước khi chạy tất cả các test
    setUpAll(() async {
      // Kết nối với ứng dụng đang chạy (thay URL bằng URL từ log flutter run)
      driver = await FlutterDriver.connect(dartVmServiceUrl: 'http://127.0.0.1:54191/gZUSCO3EuF8=/');
      // Đợi cho đến khi khung hình đầu tiên được render
      await driver.waitUntilFirstFrameRasterized();
    });

    // Dọn dẹp sau khi chạy tất cả các test
    tearDownAll(() async {
      await driver.close();
    });

    // Đặt lại trạng thái form trước mỗi test để đảm bảo tính độc lập
    setUp(() async {
      // Cuộn về đầu form để đảm bảo các trường hiển thị
      await driver.scroll(
        find.byType('SingleChildScrollView'),
        0,
        1000,
        Duration(milliseconds: 500),
      );
      // Xóa dữ liệu trong các trường nhập liệu
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('');
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('');
      // Đặt lại dropdown và radio button
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('1')); // Đặt lại về giá trị mặc định
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('1'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2025'));
      // Đặt lại giới tính
      await driver.tap(find.byValueKey('femaleRadio')); // Đặt lại về Nữ
    });

    // TC1: Kiểm tra để trống Họ
    test('TC1: Kiểm tra để trống Họ', () async {
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng nhập họ');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Vui lòng nhập họ',
        reason: 'Thông báo lỗi phải hiển thị khi để trống Họ',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC2: Kiểm tra Họ chứa ký tự đặc biệt
    test('TC2: Kiểm tra Họ chứa ký tự đặc biệt', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen@123');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Họ không được chứa ký tự đặc biệt');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Họ không được chứa ký tự đặc biệt',
        reason: 'Thông báo lỗi phải hiển thị khi Họ chứa ký tự đặc biệt',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC3: Kiểm tra Họ vượt quá 50 ký tự
    test('TC3: Kiểm tra Họ vượt quá 50 ký tự', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText(
          'NguyenVanABCD1234567890abcdefghijkLMNOPqrstuvwxyz1234567890');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Họ không được vượt quá 50 ký tự');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Họ không được vượt quá 50 ký tự',
        reason: 'Thông báo lỗi phải hiển thị khi Họ vượt quá 50 ký tự',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC16: Kiểm tra Email không hợp lệ (thiếu @)
    test('TC16: Kiểm tra Email không hợp lệ (thiếu @)', () async {
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('abc');
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Số điện thoại hoặc email không hợp lệ');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Số điện thoại hoặc email không hợp lệ',
        reason: 'Thông báo lỗi phải hiển thị khi email không hợp lệ',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC17: Kiểm tra Số điện thoại không hợp lệ (dưới 10 số)
    test('TC17: Kiểm tra Số điện thoại không hợp lệ (dưới 10 số)', () async {
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('123456789');
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Số điện thoại hoặc email không hợp lệ');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Số điện thoại hoặc email không hợp lệ',
        reason: 'Thông báo lỗi phải hiển thị khi số điện thoại dưới 10 số',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC27: Kiểm tra nhập đầy đủ và hợp lệ (Email)
    test('TC27: Kiểm tra nhập đầy đủ và hợp lệ (Email)', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final successMessage = find.text('Đăng ký thành công!');
      await driver.waitFor(successMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(successMessage),
        'Đăng ký thành công!',
        reason: 'Thông báo thành công phải hiển thị khi dữ liệu hợp lệ',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC28: Kiểm tra nhập đầy đủ và hợp lệ (Số điện thoại)
    test('TC28: Kiểm tra nhập đầy đủ và hợp lệ (Số điện thoại)', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('0901234567');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final successMessage = find.text('Đăng ký thành công!');
      await driver.waitFor(successMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(successMessage),
        'Đăng ký thành công!',
        reason: 'Thông báo thành công phải hiển thị khi số điện thoại hợp lệ',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC29: Kiểm tra giao diện logo
    test('TC29: Kiểm tra giao diện logo', () async {
      final logo = find.byValueKey('logo');
      await driver.waitFor(logo, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(logo),
        'facebook',
        reason: 'Nội dung logo phải là "facebook"',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC30: Kiểm tra tiêu đề
    test('TC30: Kiểm tra tiêu đề', () async {
      final title = find.byValueKey('title');
      await driver.waitFor(title, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(title),
        'Tạo tài khoản mới',
        reason: 'Tiêu đề phải là "Tạo tài khoản mới"',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC31: Kiểm tra phụ đề
    test('TC31: Kiểm tra phụ đề', () async {
      final subtitle = find.byValueKey('subtitle');
      await driver.waitFor(subtitle, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(subtitle),
        'Nhanh chóng và dễ dàng.',
        reason: 'Phụ đề phải là "Nhanh chóng và dễ dàng."',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC32: Kiểm tra để trống Tên
    test('TC32: Kiểm tra để trống Tên', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng nhập tên');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Vui lòng nhập tên',
        reason: 'Thông báo lỗi phải hiển thị khi để trống Tên',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC33: Kiểm tra để trống Ngày sinh
    test('TC33: Kiểm tra để trống Ngày sinh', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng chọn ngày sinh đầy đủ');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Vui lòng chọn ngày sinh đầy đủ',
        reason: 'Thông báo lỗi phải hiển thị khi để trống Ngày sinh',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC34: Kiểm tra để trống Giới tính
    test('TC34: Kiểm tra để trống Giới tính', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng chọn giới tính');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Vui lòng chọn giới tính',
        reason: 'Thông báo lỗi phải hiển thị khi để trống Giới tính',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC35: Kiểm tra để trống Mật khẩu
    test('TC35: Kiểm tra để trống Mật khẩu', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng nhập mật khẩu');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Vui lòng nhập mật khẩu',
        reason: 'Thông báo lỗi phải hiển thị khi để trống Mật khẩu',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC36: Kiểm tra Mật khẩu dưới 6 ký tự
    test('TC36: Kiểm tra Mật khẩu dưới 6 ký tự', () async {
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123');
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Mật khẩu phải có ít nhất 6 ký tự');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Mật khẩu phải có ít nhất 6 ký tự',
        reason: 'Thông báo lỗi phải hiển thị khi Mật khẩu dưới 6 ký tự',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC37: Kiểm tra ngày sinh trong tương lai
    test('TC37: Kiểm tra ngày sinh trong tương lai', () async {
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('28'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2025'));
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Ngày sinh không được trong tương lai');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Ngày sinh không được trong tương lai',
        reason: 'Thông báo lỗi phải hiển thị khi ngày sinh trong tương lai',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC38: Kiểm tra tuổi dưới 13
    test('TC38: Kiểm tra tuổi dưới 13', () async {
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('28'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2013'));
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Bạn phải từ 13 tuổi trở lên');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Bạn phải từ 13 tuổi trở lên',
        reason: 'Thông báo lỗi phải hiển thị khi tuổi dưới 13',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC39: Kiểm tra phản hồi dưới 100ms (điều chỉnh cách đo thời gian)
    test('TC39: Kiểm tra phản hồi dưới 100ms', () async {
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('dayField'));
      await driver.tap(find.text('26'));
      await driver.tap(find.byValueKey('monthField'));
      await driver.tap(find.text('5'));
      await driver.tap(find.byValueKey('yearField'));
      await driver.tap(find.text('2000'));
      await driver.tap(find.byValueKey('maleRadio'));
      await driver.tap(find.byValueKey('emailOrPhoneField'));
      await driver.enterText('test@example.com');
      await driver.tap(find.byValueKey('passwordField'));
      await driver.enterText('123456');
      await driver.tap(find.byValueKey('signupButton'));
      try {
        await driver.waitFor(find.text('Đăng ký thành công!'), timeout: Duration(milliseconds: 100));
      } catch (e) {
        fail('Phản hồi không dưới 100ms');
      }
    }, timeout: Timeout(Duration(seconds: 5)));

    // TC52: Kiểm tra viền đỏ khi để trống Họ (gián tiếp qua thông báo lỗi)
    test('TC52: Kiểm tra viền đỏ khi để trống Họ', () async {
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng nhập họ');
      await driver.waitFor(errorMessage, timeout: Duration(seconds: 5));
      expect(
        await driver.getText(errorMessage),
        'Vui lòng nhập họ',
        reason: 'Thông báo lỗi phải hiển thị khi để trống Họ, gián tiếp xác nhận viền đỏ',
      );
    }, timeout: Timeout(Duration(seconds: 10)));

    // TC53: Kiểm tra viền đen khi nhấp vào ô Họ trống
    test('TC53: Kiểm tra viền đen khi nhấp vào ô Họ trống', () async {
      await driver.tap(find.byValueKey('lastNameField'));
      await driver.enterText('Van A');
      await driver.tap(find.byValueKey('signupButton'));
      await driver.tap(find.byValueKey('firstNameField'));
      await driver.enterText('Nguyen');
      await driver.tap(find.byValueKey('signupButton'));
      final errorMessage = find.text('Vui lòng nhập họ');
      await driver.waitForAbsent(errorMessage, timeout: Duration(seconds: 5));
    }, timeout: Timeout(Duration(seconds: 10)));
  });
}