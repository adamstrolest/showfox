package com.showfox.browser.ui.browser

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.showfox.browser.R
import com.showfox.browser.ui.MainActivity
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

class BrowserFragment : Fragment() {

    companion object {
        private const val TAG = "BrowserFragment"
        private const val ARG_URL = "url"
        private const val DEFAULT_URL = "https://www.google.com"

        fun newInstance(url: String = DEFAULT_URL): BrowserFragment {
            return BrowserFragment().apply {
                arguments = Bundle().apply { putString(ARG_URL, url) }
            }
        }
    }

    private lateinit var geckoView: GeckoView
    private var session: GeckoSession? = null
    private var topBarView: TopBar? = null
    private var isTopBarShown = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_browser, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as MainActivity

        geckoView = view.findViewById(R.id.gecko_view)
        topBarView = view.findViewById(R.id.top_bar)

        session = activity.sessionManager.createSession()
        session?.let { sess ->
            activity.sessionManager.openSession(sess)
            geckoView.setSession(sess)

            val url = arguments?.getString(ARG_URL) ?: DEFAULT_URL
            activity.sessionManager.loadUrl(sess, url)
        }

        setupTopBar()
    }

    private fun setupTopBar() {
        topBarView?.apply {
            val urlBar = findViewById<EditText>(R.id.url_bar)
            urlBar.doAfterTextChanged { text ->
                if (text != null) {
                    updateUrlSuggestions(text.toString())
                }
            }
            urlBar.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_GO ||
                    (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    val url = urlBar.text.toString()
                    navigate(url)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun updateUrlSuggestions(query: String) {
        if (query.length >= 2) {
        }
    }

    fun setTopBarVisible(visible: Boolean) {
        isTopBarShown = visible
        topBarView?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun navigate(url: String) {
        session?.let { sess ->
            val activity = requireActivity() as MainActivity
            val fullUrl = normalizeUrl(url)
            activity.sessionManager.loadUrl(sess, fullUrl)
            hideTopBar()
        }
    }

    private fun normalizeUrl(input: String): String {
        val trimmed = input.trim()
        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
            else -> "https://www.google.com/search?q=${android.net.Uri.encode(trimmed)}"
        }
    }

    fun canGoBack(): Boolean = session?.canGoBack ?: false

    fun goBack() {
        session?.let { sess ->
            val activity = requireActivity() as MainActivity
            if (!activity.sessionManager.goBack(sess)) {
                (activity as MainActivity).hideTopBar()
            }
        }
    }

    fun hideTopBar() {
        if (isTopBarShown) {
            setTopBarVisible(false)
        }
    }

    fun updateNavigationState() {
        topBarView?.updateNavigationState(
            canGoBack = canGoBack(),
            canGoForward = session?.canGoForward ?: false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        session?.let { sess ->
            val activity = requireActivity() as MainActivity
            activity.sessionManager.releaseView(geckoView)
        }
    }
}
