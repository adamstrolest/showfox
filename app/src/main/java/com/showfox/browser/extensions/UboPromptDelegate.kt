package com.showfox.browser.extensions

import android.util.Log
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.WebExtension
import org.mozilla.geckoview.WebExtensionController

class UboPromptDelegate : WebExtensionController.PromptDelegate {

    companion object {
        private const val TAG = "UboPromptDelegate"
        private const val UBO_NAME = "uBlock Origin"
    }

    override fun onInstallPromptRequest(
        extension: WebExtension,
        permissions: Array<String>,
        origins: Array<String>
    ): GeckoResult<WebExtension.PermissionPromptResponse> {
        if (extension.metaData.name == UBO_NAME) {
            Log.i(TAG, "Auto-approving uBO permissions: ${permissions.joinToString()}")
            return GeckoResult.fromValue(
                WebExtension.PermissionPromptResponse(
                    granted = true,
                    grantedPermissions = true,
                    grantedOrigins = true
                )
            )
        }
        return GeckoResult.fromValue(
            WebExtension.PermissionPromptResponse(
                granted = false,
                grantedPermissions = false,
                grantedOrigins = false
            )
        )
    }

    override fun onUpdatePromptRequest(
        extension: WebExtension,
        permissions: Array<String>,
        origins: Array<String>
    ): GeckoResult<WebExtension.PermissionPromptResponse> {
        return onInstallPromptRequest(extension, permissions, origins)
    }

    override fun onOptionalPromptRequest(
        extension: WebExtension,
        permissions: Array<String>,
        origins: Array<String>
    ): GeckoResult<WebExtension.PermissionPromptResponse> {
        return onInstallPromptRequest(extension, permissions, origins)
    }
}
