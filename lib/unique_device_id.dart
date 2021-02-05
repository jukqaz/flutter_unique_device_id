import 'dart:io';

import 'package:flutter/services.dart';

class UniqueDeviceId {
  static const MethodChannel _channel = const MethodChannel('unique_device_id');

  static final _instance = UniqueDeviceId._();

  static UniqueDeviceId get instance => _instance;

  UniqueDeviceId._();

  Future<void> setUseInternalStorageForAndroid(bool use) async {
    if (!Platform.isAndroid) return;
    await _channel.invokeMethod('setUseInternalStorageForAndroid', use);
  }

  Future<String> getUniqueId() => _channel.invokeMethod('getUniqueId');
}
