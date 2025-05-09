import 'dart:convert';
import 'dart:io';
import 'dart:math';

class Question {
  final String question;
  final List<String> options;
  final int answerIndex;

  Question({
    required this.question,
    required this.options,
    required this.answerIndex,
  });

  factory Question.fromJson(Map<String, dynamic> json) {
    return Question(
      question: json['question'] as String,
      options: List<String>.from(json['options']),
      answerIndex: json['answer'] as int,
    );
  }

  Map<String, dynamic> toJson() {
    return {'question': question, 'options': options, 'answer': answerIndex};
  }
}

Future<List<Question>> loadQuestions(String path) async {
  final file = File(path);
  if (!await file.exists()) {
    throw Exception('File "$path" không tồn tại.');
  }
  final contents = await file.readAsString();
  final List<dynamic> data = json.decode(contents);
  return data.map((e) => Question.fromJson(e as Map<String, dynamic>)).toList();
}

void startQuiz(List<Question> questions) {
  final rng = Random();
  var score = 0;
  final total = questions.length;

  questions.shuffle(rng);
  for (var i = 0; i < total; i++) {
    final q = questions[i];
    print('\nCâu hỏi ${i + 1}: ${q.question}');
    for (var j = 0; j < q.options.length; j++) {
      print('  ${j + 1}. ${q.options[j]}');
    }

    stdout.write('Nhập lựa chọn của bạn (1-${q.options.length}): ');
    final input = stdin.readLineSync();
    final choice = int.tryParse(input ?? '') ?? 0;

    if (choice - 1 == q.answerIndex) {
      print('✅ Đúng rồi!');
      score++;
    } else {
      print('❌ Sai rồi! Đáp án đúng là: ${q.options[q.answerIndex]}');
    }
  }

  final percent = (score / total * 100).toStringAsFixed(1);
  print('\nKết quả của bạn: $score/$total câu ($percent%)');
}

Future<void> main(List<String> args) async {
  // Xác định file câu hỏi
  var path = args.isNotEmpty ? args[0] : 'questions.json';
  if (!await File(path).exists()) {
    stderr.writeln(
      '⚠️ File "$path" không tìm thấy, sử dụng "questions.json" mặc định.',
    );
    path = 'questions.json';
  }

  print('Đang tải câu hỏi từ "$path"...');
  try {
    final questions = await loadQuestions(path);
    startQuiz(questions);
  } catch (e) {
    stderr.writeln('❌ Lỗi khi tải câu hỏi: $e');
  }
}
