import 'package:flutter/material.dart';
import 'weather_forecast.dart';

class WeatherScreen extends StatefulWidget {
  const WeatherScreen({super.key});

  @override
  _WeatherScreenState createState() => _WeatherScreenState();
}

class _WeatherScreenState extends State<WeatherScreen> {
  final _cityController = TextEditingController();
  final _countryController = TextEditingController();
  final _latController = TextEditingController();
  final _lonController = TextEditingController();
  bool _useCoordinates = false;
  Map<String, dynamic>? _weatherData;
  String? _errorMessage;

  Future<void> _fetchWeather() async {
    setState(() {
      _errorMessage = null;
      _weatherData = null;
    });

    WeatherForecast weather;
    if (_useCoordinates) {
      try {
        final lat = double.parse(_latController.text);
        final lon = double.parse(_lonController.text);
        weather = WeatherForecast(latitude: lat, longitude: lon);
      } catch (e) {
        setState(() {
          _errorMessage = 'Vui lòng nhập tọa độ hợp lệ (số).';
        });
        return;
      }
    } else {
      final city = _cityController.text.trim();
      final country = _countryController.text.trim();
      if (city.isEmpty || country.isEmpty) {
        setState(() {
          _errorMessage = 'Vui lòng nhập đầy đủ thành phố và mã quốc gia.';
        });
        return;
      }
      weather = WeatherForecast(city: city, countryCode: country);
    }

    final data = await weather.fetchWeatherForecast();
    if (data != null) {
      await weather.saveToCsv(data);
      setState(() {
        _weatherData = data;
      });
      // Kiểm tra thời tiết xấu
      final forecasts = data['list'] as List<dynamic>;
      for (var forecast in forecasts) {
        final description = forecast['weather'][0]['description'].toString().toLowerCase();
        if (description.contains('heavy rain') || description.contains('storm')) {
          final dateTime = DateTime.fromMillisecondsSinceEpoch(forecast['dt'] * 1000);
          showDialog(
            context: context,
            builder: (context) => AlertDialog(
              title: const Text('Cảnh báo thời tiết xấu'),
              content: Text('Thời tiết xấu ($description) vào $dateTime.'),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('OK'),
                ),
              ],
            ),
          );
          break;
        }
      }
    } else {
      setState(() {
        _errorMessage = 'Không tìm thấy dữ liệu. Vui lòng kiểm tra thành phố (ví dụ: "Thanh Hoa" thay vì "Thanhhoa") hoặc tọa độ.';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Weather Forecast'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Chọn nơi xem thời tiết:',
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 10),
              Row(
                children: [
                  Expanded(
                    child: RadioListTile<bool>(
                      title: const Text('Thành phố & Mã quốc gia'),
                      value: false,
                      groupValue: _useCoordinates,
                      onChanged: (value) => setState(() => _useCoordinates = value!),
                    ),
                  ),
                  Expanded(
                    child: RadioListTile<bool>(
                      title: const Text('Tọa độ'),
                      value: true,
                      groupValue: _useCoordinates,
                      onChanged: (value) => setState(() => _useCoordinates = value!),
                    ),
                  ),
                ],
              ),
              if (!_useCoordinates) ...[
                TextField(
                  controller: _cityController,
                  decoration: const InputDecoration(
                    labelText: 'Thành phố (ví dụ: Thanh Hoa)',
                    hintText: 'Nhập tên thành phố, có dấu cách nếu cần',
                  ),
                ),
                TextField(
                  controller: _countryController,
                  decoration: const InputDecoration(
                    labelText: 'Mã quốc gia (ví dụ: VN)',
                  ),
                ),
              ] else ...[
                TextField(
                  controller: _latController,
                  decoration: const InputDecoration(
                    labelText: 'Vĩ độ (ví dụ: 19.8075)',
                  ),
                  keyboardType: TextInputType.number,
                ),
                TextField(
                  controller: _lonController,
                  decoration: const InputDecoration(
                    labelText: 'Kinh độ (ví dụ: 105.7764)',
                  ),
                  keyboardType: TextInputType.number,
                ),
              ],
              const SizedBox(height: 10),
              ElevatedButton(
                onPressed: _fetchWeather,
                child: const Text('Xem dự báo'),
              ),
              if (_errorMessage != null) ...[
                const SizedBox(height: 10),
                Text(
                  _errorMessage!,
                  style: const TextStyle(color: Colors.red),
                ),
              ],
              if (_weatherData != null) ...[
                const SizedBox(height: 20),
                Text(
                  'DỰ BÁO 5 NGÀY TẠI ${_useCoordinates ? "Tọa độ (${_latController.text}, ${_lonController.text})" : "${_cityController.text}, ${_countryController.text}"}:',
                  style: Theme.of(context).textTheme.titleMedium,
                ),
                const SizedBox(height: 10),
                _buildForecastList(),
              ],
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildForecastList() {
    final forecasts = _weatherData!['list'] as List<dynamic>;
    String? currentDate;

    return ListView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemCount: forecasts.length + 5, // Thêm 5 tiêu đề ngày
      itemBuilder: (context, index) {
        if (index >= forecasts.length) return const SizedBox.shrink();

        final forecast = forecasts[index];
        final dateTime = DateTime.fromMillisecondsSinceEpoch(forecast['dt'] * 1000);
        final date = dateTime.toString().split(' ')[0];
        final time = dateTime.toString().split(' ')[1].substring(0, 5);
        final temp = forecast['main']['temp'];
        final humidity = forecast['main']['humidity'];
        final windSpeed = forecast['wind']['speed'];
        final pressure = forecast['main']['pressure'];
        final rainProbability = (forecast['pop'] ?? 0) * 100;
        final description = forecast['weather'][0]['description'];

        if (currentDate != date) {
          currentDate = date;
          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: Text(
                  'Ngày $date',
                  style: Theme.of(context).textTheme.titleLarge,
                ),
              ),
              _buildForecastCard(time, temp, humidity, windSpeed, pressure, rainProbability, description),
            ],
          );
        }

        return _buildForecastCard(time, temp, humidity, windSpeed, pressure, rainProbability, description);
      },
    );
  }

  Widget _buildForecastCard(String time, num temp, num humidity, num windSpeed, num pressure, num rainProbability, String description) {
    final isBadWeather = description.toLowerCase().contains('heavy rain') || description.toLowerCase().contains('storm');
    return Card(
      color: isBadWeather ? Colors.red[100] : null,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Thời gian: $time', style: const TextStyle(fontWeight: FontWeight.bold)),
            Text('Nhiệt độ: $temp°C'),
            Text('Độ ẩm: $humidity%'),
            Text('Tốc độ gió: $windSpeed m/s'),
            Text('Áp suất: $pressure hPa'),
            Text('Xác suất mưa: $rainProbability%'),
            Text('Mô tả: $description'),
          ],
        ),
      ),
    );
  }
}