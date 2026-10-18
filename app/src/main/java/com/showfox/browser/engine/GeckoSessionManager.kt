package com.showfox.browser.engine

import android.content.Context
import android.util.Log
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import java.lang.ref.WeakReference

class GeckoSessionManager(private val context: Context) {

    companion object {
        private const val TAG = "GeckoSessionManager"
        private const val MAX_TABS = 5
    }

    var runtime: GeckoRuntime? = null
        private set

    private val sessions = mutableListOf<GeckoSession>()
    private val viewPool = mutableListOf<WeakReference<GeckoView>>()
    private var activeSession: GeckoSession? = null

    var onProgressChanged: ((GeckoSession, Int) -> Unit)? = null
    var onTitleChanged: ((GeckoSession, String?) -> Unit)? = null
    var onUrlChanged: ((GeckoSession, String?) -> Unit)? = null
    var onGoBackChanged: ((GeckoSession, Boolean) -> Unit)? = null
    var onGoForwardChanged: ((GeckoSession, Boolean) -> Unit)? = null
    var onSecurityChanged: ((GeckoSession, GeckoSession.SecurityInformation) -> Unit)? = null

    fun initialize(): GeckoResult<GeckoRuntime> {
        if (runtime != null) {
            return GeckoResult.fromValue(runtime)
        }

        val builder = GeckoRuntime.Settings.Builder()
            .configFilePath(context.filesDir.resolve("gecko").absolutePath)
            .javaEnabled(true)
        ContentBlocker.applyTo(builder)

        return GeckoRuntime.create(context, builder.build()).also {
            runtime = it
            Log.d(TAG, "GeckoRuntime initialized")
        }
    }

    fun createSession(): GeckoSession {
        val session = GeckoSession()
        session.contentDelegate = SessionContentDelegate(this)
        session.progressDelegate = SessionProgressDelegate(this)
        session.navigationDelegate = SessionNavigationDelegate(this)
        session.promptDelegate = SessionPromptDelegate()

        if (sessions.size >= MAX_TABS) {
            val oldest = sessions.removeFirst()
            oldest.close()
            Log.d(TAG, "Closed oldest tab, limit: $MAX_TABS")
        }

        sessions.add(session)
        return session
    }

    fun openSession(session: GeckoSession) {
        runtime?.let {
            session.open(it)
            activeSession = session
        }
    }

    fun closeSession(session: GeckoSession) {
        session.close()
        sessions.remove(session)
        if (activeSession == session) {
            activeSession = sessions.lastOrNull()
        }
    }

    fun getActiveSession(): GeckoSession? = activeSession

    fun getSessions(): List<GeckoSession> = sessions.toList()

    fun setActiveSession(session: GeckoSession) {
        activeSession = session
    }

    fun acquireView(context: Context): GeckoView {
        viewPool.removeAll { it.get() == null }
        val pooled = viewPool.firstOrNull()?.get()
        if (pooled != null) {
            return pooled
        }
        return GeckoView(context).also {
            viewPool.add(WeakReference(it))
        }
    }

    fun releaseView(view: GeckoView) {
        view.setSession(null)
    }

    fun loadUrl(session: GeckoSession, url: String) {
        session.loadUri(url)
        onUrlChanged?.invoke(session, url)
    }

    fun goBack(session: GeckoSession): Boolean {
        if (session.canGoBack) {
            session.goBack()
            return true
        }
        return false
    }

    fun goForward(session: GeckoSession): Boolean {
        if (session.canGoForward) {
            session.goForward()
            return true
        }
        return false
    }

    fun reload(session: GeckoSession) {
        session.reload()
    }
}
