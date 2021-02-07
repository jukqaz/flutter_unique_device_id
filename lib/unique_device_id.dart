import 'package:flutter/services.dart';

class UniqueDeviceId {
  static const _channel = const MethodChannel('unique_device_id');

  static final _instance = UniqueDeviceId._();

  static UniqueDeviceId get instance => _instance;

  UniqueDeviceId._();

  ///
  /// Get unique id
  ///
  /// - Android: SSAID, fallback saved random UUID
  /// - iOS: identifierForVendor
  Future<String> getUniqueId() => _channel.invokeMethod('getUniqueId');
}
