import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../viewmodels/kitchen_viewmodel.dart';
import '../../screens/menu/menu_screen.dart';

class KitchenScreen extends StatefulWidget {
  @override
  _KitchenScreenState createState() => _KitchenScreenState();
}

class _KitchenScreenState extends State<KitchenScreen> {
  Timer? _debounce;

  ScrollController _scrollController = ScrollController();
  int currentPage = 0;
  bool isLoadingMore = false;

  @override
  void initState() {
    super.initState();

    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        Provider.of<KitchenViewModel>(context, listen: false).loadMore();
      }
    });

    Future.microtask(
      () =>
          Provider.of<KitchenViewModel>(context, listen: false).loadKitchens(),
    );
  }

  @override
  Widget build(BuildContext context) {
    final vm = Provider.of<KitchenViewModel>(context);

    return Scaffold(
      appBar: AppBar(title: Text("Kitchens")),

      body: Column(
        children: [
          // ✅ SEARCH
          // TEXT FIELD
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              ElevatedButton(
                onPressed: () {
                  vm.applyFilter(null); // all
                },
                child: Text("All"),
              ),
              ElevatedButton(
                onPressed: () {
                  vm.applyFilter("VEGETARIAN");
                },
                child: Text("Veg"),
              ),
              ElevatedButton(
                onPressed: () {
                  vm.applyFilter("NON_VEGETARIAN");
                },
                child: Text("Non-Veg"),
              ),
            ],
          ),
          Padding(
            padding: const EdgeInsets.all(10),
            child: TextField(
              decoration: InputDecoration(
                hintText: "Search dishes (e.g. chicken)",
                prefixIcon: Icon(Icons.search),
                border: OutlineInputBorder(),
              ),

              // ✅ Debounce search
              onChanged: (value) {
                if (_debounce?.isActive ?? false) _debounce!.cancel();

                _debounce = Timer(const Duration(milliseconds: 500), () {
                  Provider.of<KitchenViewModel>(
                    context,
                    listen: false,
                  ).searchKitchens(value);
                });
              },
            ),
          ),

          // ✅ LIST + LOADING
          Expanded(
            child: vm.isLoading
                ? Center(child: CircularProgressIndicator())
                : ListView.builder(
                    controller: _scrollController,
                    itemCount: vm.kitchens.length,
                    itemBuilder: (context, index) {
                      final kitchen = vm.kitchens[index];

                      return GestureDetector(
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => MenuScreen(
                                kitchenId: kitchen.id,
                                kitchenName: kitchen.name,
                              ),
                            ),
                          );
                        },
                        child: Card(
                          margin: EdgeInsets.all(10),
                          child: Row(
                            children: [
                              Image.network(
                                kitchen.imageUrl,
                                width: 80,
                                height: 80,
                                fit: BoxFit.cover,
                              ),

                              SizedBox(width: 10),

                              Expanded(
                                child: ListTile(
                                  title: Text(kitchen.name),
                                  subtitle: Text(
                                    "${kitchen.area}, ${kitchen.city}",
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
