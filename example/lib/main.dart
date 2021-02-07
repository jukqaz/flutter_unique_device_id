import 'package:flutter/material.dart';
import 'package:unique_device_id/unique_device_id.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: FutureBuilder(
            future: UniqueDeviceId.instance.getUniqueId(),
            builder: (context, snapshot) =>
                snapshot.hasData ? Text('Unique ID: ${snapshot.data}\n') : CircularProgressIndicator(),
          ),
        ),
      ),
    );
  }
}
