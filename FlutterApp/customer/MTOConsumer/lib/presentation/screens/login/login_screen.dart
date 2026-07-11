import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../viewmodels/auth_viewmodel.dart';
import '../kitchen/kitchen_screen.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final phoneController = TextEditingController();
  final otpController = TextEditingController();

  bool otpSent = false;

  @override
  Widget build(BuildContext context) {
    final vm = Provider.of<AuthViewModel>(context);

    return Scaffold(
      appBar: AppBar(title: Text("Login")),
      body: Padding(
        padding: EdgeInsets.all(16),
        child: Column(children: [

          TextField(
            controller: phoneController,
            decoration: InputDecoration(labelText: "Phone Number"),
          ),

          if (otpSent)
            TextField(
              controller: otpController,
              decoration: InputDecoration(labelText: "Enter OTP"),
            ),

          SizedBox(height: 20),

          vm.isLoading
              ? CircularProgressIndicator()
              : ElevatedButton(
            onPressed: () async {
              if (!otpSent) {
                bool success =
                await vm.sendOtp(phoneController.text);

                if (success) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("OTP sent")),
                  );
                  setState(() => otpSent = true);
                }
              } else {
                String? token = await vm.verifyOtp(
                  phoneController.text,
                  otpController.text,
                );

                if (token != null) {
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(builder: (_) => KitchenScreen()),
                  );
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("Invalid OTP")),
                  );
                }
              }
            },
            child: Text(otpSent ? "Verify OTP" : "Send OTP"),
          )
        ]),
      ),
    );
  }
}
