package com.showfox.browser.ui.newtab

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.showfox.browser.R
import com.showfox.browser.ui.MainActivity
import com.showfox.browser.ui.OnScreenKeyboardHost
import com.showfox.browser.ui.browser.OnScreenKeyboard

class NewTabFragment : Fragment(), OnScreenKeyboardHost {

    private var keyboard: OnScreenKeyboard? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_new_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar = view.findViewById<EditText>(R.id.new_tab_search)
        val keyboard = view.findViewById<OnScreenKeyboard>(R.id.on_screen_keyboard)
        this.keyboard = keyboard
        keyboard.bind(searchBar) { submit(searchBar.text.toString()) }

        searchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                submit(searchBar.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun submit(query: String) {
        if (query.isNotBlank()) {
            keyboard?.visibility = View.GONE
            val url = buildUrl(query)
            (activity as MainActivity).navigateToUrl(url)
        }
    }

    override fun hideOnScreenKeyboard(): Boolean {
        val kb = keyboard ?: return false
        return if (kb.visibility == View.VISIBLE) {
            kb.visibility = View.GONE
            true
        } else {
            false
        }
    }

    private fun buildUrl(input: String): String {
        val trimmed = input.trim()
        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
            else -> "https://www.google.com/search?q=${android.net.Uri.encode(trimmed)}"
        }
    }
}
