package com.showfox.browser.engine

import android.content.Context
import android.util.Log
import org.mozilla.geckoview.GeckoSession

object CursorInjector {

    private const val TAG = "CursorInjector"

    fun inject(session: GeckoSession, context: Context) {
        if (!GeckoPrefs.isCustomCursorEnabled(context)) return
        Log.d(TAG, "Custom cursor injection is handled through the built-in cursor extension")
    }
}
