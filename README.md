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