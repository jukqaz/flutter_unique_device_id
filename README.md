# unique_device_id

Get unique device id

## Getting Started
1. getUniqueId()
   - Get unique device id (if id does not exist, generate and save uuid)
      - Android: SSAID
      - iOS: identifierForVendor<br>
`
  UniqueDeviceId.instance.getUniqueId()
`
2. setUseInternalStorageForAndroid(bool use)
   - If SSAID does not exist, save internal storage<br>
`
  await UniqueDeviceId.instance.setUseInternalStorageForAndroid(true);
`