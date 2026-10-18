package com.showfox.browser.engine

import android.util.Log
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoRuntimeSettings

object ContentBlocker {

    private const val TAG = "ContentBlocker"

    fun applyTo(builder: GeckoRuntimeSettings.Builder) {
        builder.contentBlocking(
            ContentBlocking.Settings.Builder()
                .antiTracking(ContentBlocking.AntiTracking.STRICT)
                .safeBrowsing(ContentBlocking.SafeBrowsing.DEFAULT)
                .cookieBehavior(ContentBlocking.CookieBehavior.ACCEPT_NON_TRACKERS)
                .cookieBehaviorPrivateMode(ContentBlocking.CookieBehavior.ACCEPT_NON_TRACKERS)
                .queryParameterStrippingEnabled(true)
                .queryParameterStrippingPrivateBrowsingEnabled(true)
                .emailTrackerBlockingPrivateMode(true)
                .strictSocialTrackingProtection(true)
                .build()
        )
        Log.d(TAG, "ContentBlocking configured: STRICT anti-tracking, all safe browsing")
    }

    fun getAntiTrackingMask(): Int = ContentBlocking.AntiTracking.STRICT

    fun isTrackingBlocked(category: Int): Boolean =
        (getAntiTrackingMask() and category) != 0
}
