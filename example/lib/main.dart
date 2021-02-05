import 'package:flutter/material.dart';
import 'package:unique_device_id/unique_device_id.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await UniqueDeviceId.instance.setUseInternalStorageForAndroid(true);
  debugPrint('### ${await UniqueDeviceId.instance.getUniqueId()}');

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
          child: Text('Running on: a\n'),
        ),
      ),
    );
  }
}
