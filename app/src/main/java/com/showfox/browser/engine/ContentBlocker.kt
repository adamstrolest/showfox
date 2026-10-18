package com.showfox.browser.engine

import android.util.Log
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoRuntime

object ContentBlocker {

    private const val TAG = "ContentBlocker"

    fun applyTo(builder: GeckoRuntime.Settings.Builder) {
        builder.contentBlocking(
            ContentBlocking.Settings.Builder()
                .antiTracking(ContentBlocking.AntiTracking.STRICT)
                .safeBrowsing(ContentBlocking.SafeBrowsing.ALL)
                .cookieBehavior(ContentBlocking.CookieBehavior.ACCEPT_NON_TRACKERS)
                .cookieBehaviorPrivateMode(ContentBlocking.CookieBehavior.ACCEPT_NON_TRACKERS)
                .queryParameterStrippingEnabled(true)
                .queryParameterStrippingPrivateBrowsingEnabled(true)
                .emailTrackingProtectionEnabled(true)
                .strictSocialTrackingProtection(true)
        )
        Log.d(TAG, "ContentBlocking configured: STRICT anti-tracking, all safe browsing")
    }

    fun getAntiTrackingMask(): Int = ContentBlocking.AntiTracking.STRICT

    fun isTrackingBlocked(category: Int): Boolean =
        (getAntiTrackingMask() and category) != 0
}
