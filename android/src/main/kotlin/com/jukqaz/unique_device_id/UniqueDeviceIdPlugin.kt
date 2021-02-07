package com.jukqaz.unique_device_id

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.*


/** UniqueDeviceIdPlugin */
class UniqueDeviceIdPlugin : FlutterPlugin, MethodCallHandler {
    companion object {
        private val filePath = Environment.getExternalStorageDirectory().absolutePath
        private const val fileName = "unique_device_id"
    }

    private lateinit var channel: MethodChannel
    private var context: Context? = null


    private val encryptedFile by lazy {
        context?.let {
            val file = File(filePath, fileName)
            val masterKey = MasterKey.Builder(it)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedFile.Builder(
                it,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "unique_device_id")
        channel.setMethodCallHandler(this)

        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getUniqueId" -> CoroutineScope(Dispatchers.Main).launch {
                try {
                    result.success(getUniqueId())
                } catch (e: PermissionNotGrantedException) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)

        context = null
    }

    private suspend fun getUniqueId(): String {
        var uniqueId: String? = getAndroidId()
        if (uniqueId?.isBlank() != false) {
            uniqueId = getSavedUUID()
        }
        return uniqueId
    }

    private suspend fun getSavedUUID(): String {
        var uuid = readUUIDFromInternalStorage()
        if (uuid?.isBlank() != false) {
            uuid = UUID.randomUUID().toString()
            writeUUIDToInternalStorage(uuid)
        }
        return uuid
    }

    private suspend fun readUUIDFromInternalStorage() = withContext(Dispatchers.IO) {
        if (!checkExternalStoragePermission()) throw PermissionNotGrantedException()
        try {
            encryptedFile?.openFileInput()?.bufferedReader()?.use { it.readLine() }
        } catch (e: IOException) {
            null
        }
    }

    private suspend fun writeUUIDToInternalStorage(uuid: String) = withContext(Dispatchers.IO) {
        if (!checkExternalStoragePermission()) throw PermissionNotGrantedException()
        try {
            encryptedFile?.openFileOutput()?.bufferedWriter().use { it?.write(uuid) }
        } catch (e: IOException) {
            null
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() = context?.run {
        Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    private fun checkExternalStoragePermission() =
        context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

class PermissionNotGrantedException : Exception()