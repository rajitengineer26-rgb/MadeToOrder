import 'package:flutter/material.dart';
import '../../../data/services/api_service.dart';

class MenuScreen extends StatefulWidget {
  final String kitchenId;
  final String kitchenName;

  MenuScreen({required this.kitchenId, required this.kitchenName});

  @override
  State<MenuScreen> createState() => _MenuScreenState();
}

class _MenuScreenState extends State<MenuScreen> {
  List menus = [];
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    loadMenus();
  }

  Future<void> loadMenus() async {
    final data =
    await ApiService.fetchMenuByKitchenId(widget.kitchenId);

    setState(() {
      menus = data;
      isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.kitchenName),
      ),
      body: isLoading
          ? Center(child: CircularProgressIndicator())
          : menus.isEmpty
          ? Center(child: Text("No menu available"))
          : ListView.builder(
        itemCount: menus.length,
        itemBuilder: (context, index) {
          final item = menus[index];

          return Card(
            margin: EdgeInsets.all(10),
            child: ListTile(
              leading: Image.network(
                item["imageUrl"],
                width: 60,
                height: 60,
                fit: BoxFit.cover,
              ),
              title: Text(item["name"]),
              subtitle: Text(item["description"]),
              trailing: Text("₹${item["price"]}"),
            ),
          );
        },
      ),
    );
  }
}
