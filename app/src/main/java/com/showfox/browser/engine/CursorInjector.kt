package com.showfox.browser.engine

import android.content.Context
import android.util.Log
import org.mozilla.geckoview.GeckoSession

object CursorInjector {

    private const val TAG = "CursorInjector"

    private const val CURSOR_CSS = """
        * { cursor: url('file:///android_asset/cursors/breeze.cur'), auto !important; }
        a, button, [role="button"], input, select, label, [tabindex] {
            cursor: url('file:///android_asset/cursors/breeze.cur'), pointer !important;
        }
    """

    fun inject(session: GeckoSession, context: Context) {
        if (!GeckoPrefs.isCustomCursorEnabled(context)) return

        val js = """
            (function() {
                var style = document.getElementById('showfox-cursor');
                if (!style) {
                    style = document.createElement('style');
                    style.id = 'showfox-cursor';
                    style.textContent = `$CURSOR_CSS`;
                    (document.head || document.documentElement).appendChild(style);
                }
            })();
        """.trimIndent()

        session.evaluateJS(js)
        Log.d(TAG, "Custom cursor CSS injected")
    }
}
