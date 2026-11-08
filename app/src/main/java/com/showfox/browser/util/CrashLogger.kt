package com.showfox.browser.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.util.Date

object CrashLogger {

    private const val TAG = "CrashLogger"
    private const val FILE_NAME = "showfox-crash.txt"

    fun install(context: Context) {
        val appContext = context.applicationContext
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                write(appContext, throwable)
            } catch (t: Throwable) {
                Log.e(TAG, "Failed to write crash log", t)
            }
            previous?.uncaughtException(thread, throwable)
        }
    }

    private fun write(context: Context, throwable: Throwable) {
        val text = buildString {
            appendLine("ShowFox crash report")
            appendLine(Date().toString())
            appendLine()
            appendLine("Device: ${Build.MANUFACTURER} ${Build.MODEL}")
            appendLine("Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            appendLine("ABIs: ${Build.SUPPORTED_ABIS.joinToString()}")
            appendLine()
            appendLine(Log.getStackTraceString(throwable))
        }

        runCatching {
            File(context.filesDir, "last_crash.txt").writeText(text)
        }

        runCatching {
            File(context.getExternalFilesDir(null), FILE_NAME).writeText(text)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            runCatching {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, FILE_NAME)
                    put(MediaStore.Downloads.MIME_TYPE, "text/plain")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { it.write(text.toByteArray()) }
                    values.clear()
                    values.put(MediaStore.Downloads.IS_PENDING, 0)
                    resolver.update(uri, values, null, null)
                }
            }
        }
    }
}
