import Flutter
import SwiftKeychainWrapper
import UIKit

public class SwiftUniqueDeviceIdPlugin: NSObject, FlutterPlugin {
  let savedUniqueIdKey = "UniqueId"

  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "unique_device_id", binaryMessenger: registrar.messenger())
    let instance = SwiftUniqueDeviceIdPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getUniqueId":
      result(getUniqueId())
    default:
      result(nil)
    }
  }

  private func getUniqueId() -> String? {
    guard let saveUniqueId = KeychainWrapper.standard.string(forKey: savedUniqueIdKey), !saveUniqueId.isEmpty else {
      guard let uuid = UIDevice.current.identifierForVendor?.uuidString, !uuid.isEmpty else {
        let generateUUID = UUID().uuidString
        setUUIDIntoKeychain(uuid: generateUUID)
        return generateUUID
      }
      setUUIDIntoKeychain(uuid: uuid)
      return uuid
    }
    return saveUniqueId
  }

  private func setUUIDIntoKeychain(uuid: String?) {
    if let nonNullUUID = uuid, !nonNullUUID.isEmpty {
      KeychainWrapper.standard.set(nonNullUUID, forKey: savedUniqueIdKey)
    }
  }
}
