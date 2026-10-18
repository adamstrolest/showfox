package com.showfox.browser.engine

import android.util.Log
import org.mozilla.geckoview.GeckoSession

object CursorInjector {

    private const val TAG = "CursorInjector"
    private const val CURSOR_CSS = """
        * { cursor: url('file:///android_asset/cursors/breeze.cur'), auto !important; }
        a, button, [role="button"], input, select, label, [tabindex] { 
            cursor: url('file:///android_asset/cursors/breeze.cur'), pointer !important; 
        }
    """.trimIndent()

    fun inject(session: GeckoSession) {
        if (!GeckoPrefs.isCustomCursorEnabled(session.contextOrActivityContext())) return

        val js = """
            (function() {
                var style = document.getElementById('showfox-cursor');
                if (!style) {
                    style = document.createElement('style');
                    style.id = 'showfox-cursor';
                    style.textContent = `$CURSOR_CSS`;
                    document.head.appendChild(style);
                }
            })();
        """.trimIndent()

        session.loadUri("javascript:$js")
        Log.d(TAG, "Custom cursor CSS injected")
    }

    private fun GeckoSession.contextOrActivityContext(): android.content.Context {
        return try {
            val field = this::class.java.getDeclaredField("context")
            field.isAccessible = true
            field.get(this) as android.content.Context
        } catch (e: Exception) {
            android.app.Application.getProcessName()?.let {
                Log.w(TAG, "Could not get context from session", e)
            }
            throw RuntimeException("Context not available", e)
        }
    }
}
