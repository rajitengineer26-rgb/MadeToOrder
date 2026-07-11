import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../../util/util.dart';

class ApiService {
  static const String baseUrl = "http://10.0.2.2:8080/api/v1";

  // ✅ Send OTP API
  static Future<Map<String, dynamic>> sendOtp(String phone) async {
    final response = await http.post(
      Uri.parse("$baseUrl/auth/otp/send"),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({"phoneNumber": phone, "purpose": "LOGIN"}),
    );

    print("SEND OTP RESPONSE: ${response.body}");

    return jsonDecode(response.body);
  }

  // ✅ Verify OTP API
  static Future<Map<String, dynamic>> verifyOtp(
    String phone,
    String otp,
  ) async {
    final response = await http.post(
      Uri.parse("$baseUrl/auth/otp/verify"),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({"phoneNumber": phone, "otp": otp}),
    );

    print("VERIFY OTP RESPONSE: ${response.body}");

    return jsonDecode(response.body);
  }

  static Future<List<dynamic>> fetchKitchens({
    String? type,
    String? search,
    int page = 0,
    int size = 10,
  }) async {

    String? token = await getAccessToken();

    // ✅ Build arguments
    String filterPart = "";
    if (type != null) {
      filterPart = 'filter: { type: $type },'; // enum → no quotes ✅
    }

    String searchPart = "";
    if (search != null && search.isNotEmpty) {
      searchPart = 'search: "$search",';
    }

    // ✅ Build GraphQL query
    String query = """
    query {
      kitchens(
        $filterPart
        $searchPart
        pagination: { page: $page, size: $size }
      ) {
        id
        name
        type
        area
        city
        imageUrl
        rating {
          average
        }
      }
    }
  """;

    // ✅ REQUEST LOG
    print("========== GRAPHQL REQUEST ==========");
    print("URL: http://10.0.2.2:8080/graphql");
    print("HEADERS:");
    print({
      "Content-Type": "application/json",
      "Authorization": "Bearer $token",
    });
    print("QUERY:");
    print(query);
    print("=====================================");

    final response = await http.post(
      Uri.parse("http://10.0.2.2:8080/graphql"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer $token",
      },
      body: jsonEncode({
        "query": query,
      }),
    );

    // ✅ RESPONSE LOG
    print("========== GRAPHQL RESPONSE ==========");
    print("STATUS CODE: ${response.statusCode}");

    try {
      final decoded = jsonDecode(response.body);

      print("BODY:");
      print(const JsonEncoder.withIndent('  ').convert(decoded));

      if (decoded["errors"] != null) {
        print("❌ GraphQL Errors:");
        print(decoded["errors"]);
      }
    } catch (e) {
      print("Error parsing response: $e");
      print("RAW BODY: ${response.body}");
    }

    print("======================================");

    // ✅ Handle token expiry
    if (response.statusCode == 401) {

      print("Access token expired. Refreshing...");

      String? newToken = await refreshAccessToken();

      if (newToken != null && newToken.isNotEmpty) {

        await Util.savAccessToken(newToken);

        // ✅ IMPORTANT: retry with SAME params
        return await fetchKitchens(
          type: type,
          search: search,
          page: page,
          size: size,
        );

      } else {
        print("Refresh failed → logout");
        return [];
      }
    }

    final data = jsonDecode(response.body);
    return data["data"]?["kitchens"] ?? [];
  }

  static Future<List<dynamic>> fetchMenuByKitchenId(String kitchenId) async {
    String? token = await getAccessToken();

    final response = await http.post(
      Uri.parse("http://10.0.2.2:8080/graphql"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer $token",
      },
      body: jsonEncode({
        "query":
            """
        query {
          menuItems(kitchenId: "$kitchenId") {
            id
            name
            description
            price
            imageUrl
            category
            type
          }
        }
      """,
      }),
    );

    if (response.statusCode == 401) {
      String? newToken = await refreshAccessToken();

      if (newToken != null) {
        Util.savAccessToken(newToken);
        return await fetchMenuByKitchenId(kitchenId);
      } else {
        print("Refresh failed → logout");
        return [];
      }
    }

    final data = jsonDecode(response.body);
    return data["data"]["menuItems"];
  }

  static Future<String?> getAccessToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString("accessToken");
  }

  static Future<String?> refreshAccessToken() async {
    final prefs = await SharedPreferences.getInstance();
    final refreshToken = prefs.getString("refreshToken");

    if (refreshToken == null) return null;

    final response = await http.post(
      Uri.parse("$baseUrl/auth/refresh"),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({"refreshToken": refreshToken}),
    );

    final data = jsonDecode(response.body);

    if (response.statusCode == 200) {
      String newAccessToken = data["accessToken"];

      await prefs.setString("accessToken", newAccessToken);

      return newAccessToken;
    }

    return null;
  }
}
