package com.showfox.browser

import android.app.Application
import android.util.Log
import com.showfox.browser.engine.GeckoSessionManager
import com.showfox.browser.extensions.ExtensionManager
import com.showfox.browser.extensions.UboUpdater

class TvBrowserApp : Application() {

    companion object {
        private const val TAG = "TvBrowserApp"
    }

    lateinit var sessionManager: GeckoSessionManager
        private set

    lateinit var extensionManager: ExtensionManager
        private set

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ShowFox starting...")

        sessionManager = GeckoSessionManager(this)
        extensionManager = ExtensionManager(this)

        sessionManager.initialize().accept({ runtime ->
            if (runtime != null) {
                Log.d(TAG, "GeckoRuntime ready")
                extensionManager.installUbo(runtime)
                UboUpdater.schedule(this)
            }
        }, { e ->
            Log.e(TAG, "Failed to initialize GeckoRuntime", e)
        })
    }
}
