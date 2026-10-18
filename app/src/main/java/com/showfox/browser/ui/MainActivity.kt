package com.showfox.browser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.showfox.browser.R
import com.showfox.browser.engine.GeckoSessionManager
import com.showfox.browser.ui.browser.BrowserFragment
import com.showfox.browser.ui.newtab.NewTabFragment
import com.showfox.browser.voice.VoiceSearchManager

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val ACTION_VOICE_SEARCH = "com.showfox.browser.ACTION_VOICE_SEARCH"
    }

    lateinit var sessionManager: GeckoSessionManager
        private set

    private var isTopBarVisible = false
    private var currentUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = GeckoSessionManager(applicationContext)
        sessionManager.onUrlChanged = { _, url -> currentUrl = url }
        sessionManager.onGoBackChanged = { _, _ -> updateBackState() }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewTabFragment())
                .commit()
        }
    }

    fun navigateToUrl(url: String) {
        val fragment = BrowserFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun toggleTopBar() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is BrowserFragment) {
            isTopBarVisible = !isTopBarVisible
            fragment.setTopBarVisible(isTopBarVisible)
            if (!isTopBarVisible) {
                Log.d(TAG, "Top bar hidden")
            }
        }
    }

    fun showTopBar() {
        if (!isTopBarVisible) {
            toggleTopBar()
        }
    }

    fun hideTopBar() {
        if (isTopBarVisible) {
            toggleTopBar()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_INFO -> {
                toggleTopBar()
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                if (isTopBarVisible) {
                    hideTopBar()
                    return true
                }
            }
            KeyEvent.KEYCODE_BACK -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.container)
                if (fragment is BrowserFragment && fragment.canGoBack()) {
                    fragment.goBack()
                    return true
                }
                if (isTopBarVisible) {
                    hideTopBar()
                    return true
                }
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VoiceSearchManager.VOICE_REQUEST_CODE) {
            val query = data?.getStringExtra(Intent.EXTRA_TEXT)
            if (!query.isNullOrBlank()) {
                val url = buildSearchUrl(query)
                navigateToUrl(url)
            }
        }
    }

    private fun buildSearchUrl(query: String): String {
        return "https://www.google.com/search?q=${android.net.Uri.encode(query)}"
    }

    fun getCurrentUrl(): String? = currentUrl

    fun canGoBack(): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        return (fragment as? BrowserFragment)?.canGoBack() ?: false
    }

    private fun updateBackState() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? BrowserFragment)?.updateNavigationState()
    }
}
