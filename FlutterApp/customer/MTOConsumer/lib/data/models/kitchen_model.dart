class Kitchen {
  final String id;
  final String name;
  final String type;
  final String area;
  final String city;
  final String imageUrl;

  Kitchen({
    required this.id,
    required this.name,
    required this.type,
    required this.area,
    required this.city,
    required this.imageUrl,
  });

  factory Kitchen.fromJson(Map<String, dynamic> json) {
    return Kitchen(
      id: json['id'],
      name: json['name'] ?? "Unknown",
      type: json['type'] ?? "Unknown",
      area: json['area']?? "Unknown",
      city: json['city']?? "Unknown",
      imageUrl: json['imageUrl']?? "https://picsum.photos/200/300",
    );
  }
}
