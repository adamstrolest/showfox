package com.showfox.browser.extensions

import android.util.Log
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.WebExtension
import org.mozilla.geckoview.WebExtensionController

class UboPromptDelegate : WebExtensionController.PromptDelegate {

    companion object {
        private const val TAG = "UboPromptDelegate"
        private const val UBO_NAME = "uBlock Origin"
    }

    private fun shouldAutoApprove(extension: WebExtension): Boolean =
        extension.metaData.name == UBO_NAME

    override fun onInstallPromptRequest(
        extension: WebExtension,
        permissions: Array<String>,
        origins: Array<String>,
        dataCollectionPermissions: Array<String>
    ): GeckoResult<WebExtension.PermissionPromptResponse> {
        val auto = shouldAutoApprove(extension)
        Log.i(TAG, "Install prompt for ${extension.metaData.name}: auto=$auto, " +
            "perms=${permissions.size}, origins=${origins.size}")
        return GeckoResult.fromValue(
            WebExtension.PermissionPromptResponse(auto, auto, false)
        )
    }

    override fun onUpdatePrompt(
        extension: WebExtension,
        newPermissions: Array<String>,
        newOrigins: Array<String>,
        dataCollectionPermissions: Array<String>
    ): GeckoResult<AllowOrDeny>? {
        val auto = shouldAutoApprove(extension)
        Log.i(TAG, "Update prompt for ${extension.metaData.name}: auto=$auto")
        return GeckoResult.fromValue(if (auto) AllowOrDeny.ALLOW else AllowOrDeny.DENY)
    }

    override fun onOptionalPrompt(
        extension: WebExtension,
        permissions: Array<String>,
        origins: Array<String>,
        dataCollectionPermissions: Array<String>
    ): GeckoResult<AllowOrDeny> {
        val auto = shouldAutoApprove(extension)
        Log.i(TAG, "Optional prompt for ${extension.metaData.name}: auto=$auto")
        return GeckoResult.fromValue(if (auto) AllowOrDeny.ALLOW else AllowOrDeny.DENY)
    }
}
