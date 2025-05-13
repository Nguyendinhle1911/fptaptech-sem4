import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:path_provider/path_provider.dart';

class WeatherForecast {
  final String apiKey = 'd4164bfbd3dc60a477ea6f1cca4524a1';
  final String? city;
  final String? countryCode;
  final double? latitude;
  final double? longitude;
  final String baseUrl = 'https://api.openweathermap.org/data/2.5/forecast';

  WeatherForecast({this.city, this.countryCode, this.latitude, this.longitude});

  Future<Map<String, dynamic>?> fetchWeatherForecast() async {
    try {
      // Kiểm tra cache
      final prefs = await SharedPreferences.getInstance();
      final cacheKey = city != null ? 'weather:$city:$countryCode' : 'weather:$latitude:$longitude';
      final cachedData = prefs.getString(cacheKey);
      if (cachedData != null) {
        final data = jsonDecode(cachedData);
        final cacheTime = prefs.getInt('$cacheKey:time') ?? 0;
        if (DateTime.now().millisecondsSinceEpoch - cacheTime < 3600 * 1000) {
          return data;
        }
      }

      // Xây dựng URL API
      String url;
      if (city != null && countryCode != null) {
        url = '$baseUrl?q=$city,$countryCode&appid=$apiKey&units=metric';
      } else {
        url = '$baseUrl?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric';
      }

      // Gọi API
      final response = await http.get(Uri.parse(url));
      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        // Lưu vào cache
        await prefs.setString(cacheKey, response.body);
        await prefs.setInt('$cacheKey:time', DateTime.now().millisecondsSinceEpoch);
        return data;
      } else {
        return null; // Lỗi 404 hoặc khác
      }
    } catch (e) {
      return null;
    }
  }

  Future<void> saveToCsv(Map<String, dynamic> data) async {
    try {
      final directory = await getApplicationDocumentsDirectory();
      final file = File('${directory.path}/weather_forecast.csv');
      final sink = file.openWrite();
      sink.write('Ngày,Thời gian,Nhiệt độ (°C),Độ ẩm (%),Tốc độ gió (m/s),Áp suất (hPa),Xác suất mưa (%),Mô tả\n');

      final forecasts = data['list'] as List<dynamic>;
      for (var forecast in forecasts) {
        final dateTime = DateTime.fromMillisecondsSinceEpoch(forecast['dt'] * 1000);
        final date = dateTime.toString().split(' ')[0];
        final time = dateTime.toString().split(' ')[1].substring(0, 5);
        final temp = forecast['main']['temp'];
        final humidity = forecast['main']['humidity'];
        final windSpeed = forecast['wind']['speed'];
        final pressure = forecast['main']['pressure'];
        final rainProbability = (forecast['pop'] ?? 0) * 100;
        final description = forecast['weather'][0]['description'];
        sink.write('$date,$time,$temp,$humidity,$windSpeed,$pressure,$rainProbability,$description\n');
      }

      sink.close();
    } catch (e) {
      // Xử lý lỗi lưu file
    }
  }
}