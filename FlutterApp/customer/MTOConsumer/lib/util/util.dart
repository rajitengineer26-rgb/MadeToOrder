import 'package:shared_preferences/shared_preferences.dart';

final class Util {
  static Future<void> saveTokens(
    String accessToken,
    String refreshToken,
  ) async {
    final prefs = await SharedPreferences.getInstance();

    await prefs.setString("accessToken", accessToken);
    await prefs.setString("refreshToken", refreshToken);
  }

  static Future<void> savAccessToken(
    String accessToken,
  ) async {
    final prefs = await SharedPreferences.getInstance();

    await prefs.setString("accessToken", accessToken);
  }

  static Future<String?> getAccessToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString("accessToken");
  }
}
