import 'package:flutter/material.dart';
import '../../data/services/api_service.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../util/util.dart';


class AuthViewModel extends ChangeNotifier {
  bool isLoading = false;

  Future<bool> sendOtp(String phone) async {
    isLoading = true;
    notifyListeners();

    final result = await ApiService.sendOtp(phone);

    isLoading = false;
    notifyListeners();

    return result["success"] == true;
  }

  Future<String?> verifyOtp(String phone, String otp) async {
    isLoading = true;
    notifyListeners();

    final result = await ApiService.verifyOtp(phone, otp);

    isLoading = false;
    notifyListeners();
    /*
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("expiresIn", 86400);
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("role", user.getRole().name());
        userData.put("isProfileComplete", user.getEmail() != null && !user.getEmail().isEmpty());
        data.put("user", userData);
        response.put("data", data);
 */
    if (result["success"] == true) {
      String accessToken = result["data"]["accessToken"];
      String refreshToken = result["data"]["refreshToken"];
      Util.saveTokens(accessToken, refreshToken);


      return accessToken;
    }

    return null;
  }
}
