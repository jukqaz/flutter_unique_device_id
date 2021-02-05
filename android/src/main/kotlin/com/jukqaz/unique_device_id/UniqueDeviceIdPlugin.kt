package com.jukqaz.unique_device_id

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.util.*

/** UniqueDeviceIdPlugin */
class UniqueDeviceIdPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private var context: Context? = null
    private var useInternalStorageForAndroid = false

    private val uuidFilePath
        get() = context?.externalCacheDir?.path + "/unique_device_id"

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "unique_device_id")
        channel.setMethodCallHandler(this)

        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) =
        when (call.method) {
            "setUseInternalStorageForAndroid" -> {
                useInternalStorageForAndroid = (call.arguments as? Boolean?) ?: false
                result.success(null)
            }
            "getUniqueId" -> result.success(getUniqueId())
            else -> result.notImplemented()
        }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)

        context = null
    }

    private fun getUniqueId(): String? {
        var uniqueId: String? = getAndroidId()
        if (uniqueId?.isBlank() == true) {
            if (!useInternalStorageForAndroid) return null
            uniqueId = getSavedUUID()
        }
        return uniqueId
    }

    private fun getSavedUUID(): String {
        var uuid = readUUIDFromInternalStorage()
        if (uuid?.isBlank() != false) {
            uuid = UUID.randomUUID().toString()
            writeUUIDToInternalStorage(uuid)
        }
        return uuid
    }

    private fun readUUIDFromInternalStorage() =
        File(uuidFilePath).takeIf { it.exists() }?.readText(Charsets.UTF_8)

    private fun writeUUIDToInternalStorage(uuid: String) =
        File(uuidFilePath).writeText(uuid, Charsets.UTF_8)

    @SuppressLint("HardwareIds")
    private fun getAndroidId() = context?.run {
        Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}
