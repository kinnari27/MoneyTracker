package com.moneytracker.data.local

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageStorageHelper {
    fun saveImageToInternalStorage(context: Context, uri: Uri, userId: Int): String? {
        return try {
            val directory = File(context.filesDir, "profile_pictures")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val destinationFile = File(directory, "profile_${userId}_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            destinationFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
