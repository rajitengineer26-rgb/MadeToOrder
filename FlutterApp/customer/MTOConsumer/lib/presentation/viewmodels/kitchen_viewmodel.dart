import 'package:flutter/material.dart';
import '../../data/services/api_service.dart';
import '../../data/models/kitchen_model.dart';

class KitchenViewModel extends ChangeNotifier {
  List<Kitchen> kitchens = [];
  bool isLoading = false;

  bool isLoadingMore = false;
  int currentPage = 0;

  Future<void> loadKitchens() async {
    isLoading = true;
    notifyListeners();

    final data = await ApiService.fetchKitchens();

    kitchens = data.map<Kitchen>((k) => Kitchen.fromJson(k)).toList();

    isLoading = false;
    notifyListeners();
  }

  Future<void> searchKitchens(String searchQuery) async {
    isLoading = true;
    notifyListeners();
    if (searchQuery.isEmpty) {
      await loadKitchens();
    } else {
      final data = await ApiService.fetchKitchens(search: searchQuery);

      kitchens = data.map<Kitchen>((k) => Kitchen.fromJson(k)).toList();
    }

    isLoading = false;
    notifyListeners();
  }

  Future<void> applyFilter(String? type) async {
    isLoading = true;
    notifyListeners();

    final data = await ApiService.fetchKitchens(type: type);

    kitchens = data.map<Kitchen>((k) => Kitchen.fromJson(k)).toList();

    isLoading = false;
    notifyListeners();
  }

  Future<void> loadMore() async {
    if (isLoadingMore) return;

    isLoadingMore = true;
    notifyListeners();

    currentPage++;

    final data = await ApiService.fetchKitchens(page: currentPage);

    kitchens.addAll(data.map<Kitchen>((k) => Kitchen.fromJson(k)).toList());

    isLoadingMore = false;
    notifyListeners();
  }
}
