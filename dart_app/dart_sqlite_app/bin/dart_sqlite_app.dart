import 'dart:io';

//==============================
// LỚP CHA TRỪU TƯỢNG: PET
//==============================
abstract class Pet {
  String name;
  int age;

  Pet(this.name, this.age);

  void makeSound(); // phương thức trừu tượng

  void printInfo() {
    print('📋 Tên: $name');
    print('🎂 Tuổi: $age');
    print('🔊 Tiếng kêu:');
    makeSound();
  }
}

//==============================
// LỚP CON: DOG
//==============================
class Dog extends Pet {
  Dog(super.name, super.age);

  @override
  void makeSound() {
    print('🐶 Gâu gâu!');
  }
}

//==============================
// LỚP CON: CAT
//==============================
class Cat extends Pet {
  Cat(super.name, super.age);

  @override
  void makeSound() {
    print('🐱 Meo meo!');
  }
}

//==============================
// DANH SÁCH THÚ CƯNG
//==============================
List<Pet> petList = [];

//==============================
// THÊM THÚ CƯNG MỚI
//==============================
void createPet() {
  print('\n➕ THÊM THÚ CƯNG MỚI');
  stdout.write('👉 Nhập tên: ');
  String name = stdin.readLineSync() ?? '';

  stdout.write('👉 Nhập tuổi: ');
  int? age = int.tryParse(stdin.readLineSync() ?? '');

  if (age == null || age < 0) {
    print('❌ Tuổi không hợp lệ.');
    return;
  }

  print('👉 Chọn loại thú cưng:');
  print('1. Chó');
  print('2. Mèo');
  stdout.write('👉 Nhập số: ');
  String? type = stdin.readLineSync();

  switch (type) {
    case '1':
      petList.add(Dog(name, age));
      print('✅ Đã thêm chó: $name');
      break;
    case '2':
      petList.add(Cat(name, age));
      print('✅ Đã thêm mèo: $name');
      break;
    default:
      print('❌ Lựa chọn không hợp lệ.');
  }
}

//==============================
// HIỂN THỊ DANH SÁCH THÚ CƯNG
//==============================
void readPets() {
  print('\n📚 DANH SÁCH THÚ CƯNG');

  if (petList.isEmpty) {
    print('⚠️ Danh sách trống.');
    return;
  }

  for (int i = 0; i < petList.length; i++) {
    print('\n🆔 ID: $i');
    petList[i].printInfo();
  }
}

//==============================
// CẬP NHẬT THÚ CƯNG
//==============================
void updatePet() {
  readPets();

  stdout.write('\n🛠 Nhập ID thú cưng cần cập nhật: ');
  int id = int.tryParse(stdin.readLineSync() ?? '') ?? -1;

  if (id < 0 || id >= petList.length) {
    print('❌ ID không hợp lệ.');
    return;
  }

  stdout.write('👉 Nhập tên mới: ');
  String name = stdin.readLineSync() ?? '';

  stdout.write('👉 Nhập tuổi mới: ');
  int? age = int.tryParse(stdin.readLineSync() ?? '');

  if (age == null || age < 0) {
    print('❌ Tuổi không hợp lệ.');
    return;
  }

  Pet current = petList[id];
  if (current is Dog) {
    petList[id] = Dog(name, age);
  } else if (current is Cat) {
    petList[id] = Cat(name, age);
  }

  print('✅ Cập nhật thành công.');
}

//==============================
// XÓA THÚ CƯNG
//==============================
void deletePet() {
  readPets();

  stdout.write('\n🗑 Nhập ID thú cưng cần xóa: ');
  int id = int.tryParse(stdin.readLineSync() ?? '') ?? -1;

  if (id < 0 || id >= petList.length) {
    print('❌ ID không hợp lệ.');
    return;
  }

  Pet removed = petList.removeAt(id);
  print('✅ Đã xóa thú cưng: ${removed.name}');
}

//==============================
// MENU CHÍNH
//==============================
void main() {
  while (true) {
    print('\n==============================');
    print('🐾 SỔ TAY THÚ CƯNG - MENU');
    print('1. Thêm thú cưng');
    print('2. Xem danh sách');
    print('3. Cập nhật');
    print('4. Xóa');
    print('5. Thoát');
    print('==============================');
    stdout.write('👉 Nhập lựa chọn: ');

    String? input = stdin.readLineSync();
    switch (input) {
      case '1':
        createPet();
        break;
      case '2':
        readPets();
        break;
      case '3':
        updatePet();
        break;
      case '4':
        deletePet();
        break;
      case '5':
        print('👋 Tạm biệt!');
        exit(0);
      default:
        print('❌ Lựa chọn không hợp lệ.');
    }
  }
}
