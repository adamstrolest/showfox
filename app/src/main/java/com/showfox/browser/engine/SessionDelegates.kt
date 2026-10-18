package com.showfox.browser.engine

import android.util.Log
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebResponse

class SessionContentDelegate(private val manager: GeckoSessionManager) : GeckoSession.ContentDelegate {

    private val TAG = "SessionContentDelegate"

    override fun onTitleChange(session: GeckoSession, title: String?) {
        Log.d(TAG, "Title changed: $title")
        manager.onTitleChanged?.invoke(session, title)
    }

    override fun onFullScreen(session: GeckoSession, fullScreen: Boolean) {
        Log.d(TAG, "Fullscreen: $fullScreen")
    }

    override fun onContextMenu(
        session: GeckoSession,
        screenX: Int,
        screenY: Int,
        element: GeckoSession.ContentDelegate.ContextElement
    ) {
        Log.d(TAG, "Context menu at $screenX,$screenY: ${element.type}")
    }

    override fun onFirstContentfulPaint(session: GeckoSession) {
        Log.d(TAG, "First contentful paint")
        CursorInjector.inject(session)
    }

    override fun onPaintStatusReset(session: GeckoSession) {
        Log.d(TAG, "Paint status reset")
    }
}

class SessionProgressDelegate(private val manager: GeckoSessionManager) : GeckoSession.ProgressDelegate {

    private val TAG = "SessionProgressDelegate"

    override fun onPageStart(session: GeckoSession, url: String) {
        Log.d(TAG, "Page start: $url")
        manager.onUrlChanged?.invoke(session, url)
    }

    override fun onPageStop(session: GeckoSession, success: Boolean) {
        Log.d(TAG, "Page stop, success: $success")
        CursorInjector.inject(session)
    }

    override fun onProgressChange(session: GeckoSession, progress: Int) {
        Log.d(TAG, "Progress: $progress%")
        manager.onProgressChanged?.invoke(session, progress)
    }

    override fun onSecurityChange(
        session: GeckoSession,
        security: GeckoSession.SecurityInformation
    ) {
        Log.d(TAG, "Security: ${security.isSecure}")
        manager.onSecurityChanged?.invoke(session, security)
    }
}

class SessionNavigationDelegate(private val manager: GeckoSessionManager) : GeckoSession.NavigationDelegate {

    private val TAG = "SessionNavigationDelegate"

    override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
        manager.onGoBackChanged?.invoke(session, canGoBack)
    }

    override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
        manager.onGoForwardChanged?.invoke(session, canGoForward)
    }

    override fun onLoadRequest(
        session: GeckoSession,
        request: GeckoSession.NavigationDelegate.LoadRequest
    ): GeckoResult<WebResponse>? {
        Log.d(TAG, "Load request: ${request.uri}")
        return null
    }

    override fun onLocationChange(session: GeckoSession, url: String?) {
        Log.d(TAG, "Location change: $url")
        manager.onUrlChanged?.invoke(session, url)
    }
}

class SessionPromptDelegate : GeckoSession.PromptDelegate {
    override fun onAlertPrompt(
        session: GeckoSession,
        prompt: GeckoSession.PromptDelegate.AlertPrompt
    ): GeckoResult<PromptResponse> {
        return GeckoResult.fromValue(prompt.dismiss())
    }

    override fun onButtonPrompt(
        session: GeckoSession,
        prompt: GeckoSession.PromptDelegate.ButtonPrompt
    ): GeckoResult<PromptResponse> {
        return GeckoResult.fromValue(prompt.dismiss())
    }

    override fun onTextPrompt(
        session: GeckoSession,
        prompt: GeckoSession.PromptDelegate.TextPrompt
    ): GeckoResult<PromptResponse> {
        return GeckoResult.fromValue(prompt.dismiss())
    }
}
