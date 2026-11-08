package com.showfox.browser

import android.app.Application
import android.util.Log
import com.showfox.browser.engine.GeckoSessionManager
import com.showfox.browser.extensions.ExtensionManager
import com.showfox.browser.extensions.UboUpdater
import com.showfox.browser.util.CrashLogger

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
        CrashLogger.install(this)
        Log.d(TAG, "ShowFox starting...")

        sessionManager = GeckoSessionManager(this)
        extensionManager = ExtensionManager(this)

        try {
            sessionManager.initialize().accept({ runtime ->
                if (runtime != null) {
                    Log.d(TAG, "GeckoRuntime ready")
                    try {
                        extensionManager.installUbo(runtime)
                        extensionManager.installCursorExtension(runtime)
                        UboUpdater.schedule(this)
                    } catch (t: Throwable) {
                        Log.e(TAG, "Post-init step failed", t)
                    }
                }
            }, { e ->
                Log.e(TAG, "Failed to initialize GeckoRuntime", e)
            })
        } catch (t: Throwable) {
            Log.e(TAG, "GeckoRuntime.create threw", t)
        }
    }
}
