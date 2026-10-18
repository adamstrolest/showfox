package com.showfox.browser.extensions

import android.content.Context
import android.util.Log
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.WebExtension
import java.io.File
import java.io.FileOutputStream

class ExtensionManager(private val context: Context) {

    companion object {
        private const val TAG = "ExtensionManager"
        private const val UBO_ID = "uBlock0@raymondhill.net"
        private const val UBO_XPI_ASSET = "ublock_origin-1.67.xpi"
    }

    var onUboStatusChanged: ((Boolean, String?) -> Unit)? = null

    fun installUbo(runtime: GeckoRuntime) {
        val promptDelegate = UboPromptDelegate()
        runtime.webExtensionController.promptDelegate = promptDelegate

        val xpiFile = copyAssetToFile(UBO_XPI_ASSET)
        if (xpiFile == null) {
            Log.e(TAG, "Failed to copy uBO xpi from assets")
            onUboStatusChanged?.invoke(false, "Failed to load extension file")
            return
        }

        val extensionResult = runtime.webExtensionController
            .install("resource://android/assets/$UBO_XPI_ASSET")

        extensionResult.accept({ extension ->
            if (extension != null) {
                Log.i(TAG, "uBO installed: ${extension.metaData.name} ${extension.metaData.version}")
                onUboStatusChanged?.invoke(true, extension.metaData.version)
            }
        }, { e ->
            Log.e(TAG, "Failed to install uBO", e)
            onUboStatusChanged?.invoke(false, e?.message ?: "Unknown error")
        })
    }

    private fun copyAssetToFile(assetName: String): File? {
        return try {
            val outFile = File(context.filesDir, assetName)
            if (outFile.exists() && outFile.length() > 0) {
                return outFile
            }
            context.assets.open(assetName).use { input ->
                FileOutputStream(outFile).use { output ->
                    input.copyTo(output)
                }
            }
            Log.d(TAG, "Copied $assetName to ${outFile.absolutePath}")
            outFile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy asset: $assetName", e)
            null
        }
    }
}
