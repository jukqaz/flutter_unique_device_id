# unique_device_id

Get unique device id

## Notice for android
1. Require minSdk 23, WRITE_EXTERNAL_STORAGE permission
2. If SSAID cannot be retrieved, a random UUID can be generated and stored in an encrypted file for use. 

## Getting Started
1. getUniqueId()
   - Get unique device id (if id does not exist, generate and save uuid)
      - Android: SSAID
      - iOS: identifierForVendor<br>
`
  UniqueDeviceId.instance.getUniqueId()
`
