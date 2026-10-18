package com.showfox.browser.ui.browser

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import com.showfox.browser.databinding.ViewUrlBarBinding

class UrlBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewUrlBarBinding.inflate(LayoutInflater.from(context), this, true)

    val editText: EditText get() = binding.urlInput

    fun setOnGoListener(listener: (String) -> Unit) {
        binding.urlInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                listener(binding.urlInput.text.toString())
                true
            } else {
                false
            }
        }
    }

    fun getUrl(): String = binding.urlInput.text.toString()

    fun setUrl(url: String) {
        binding.urlInput.setText(url)
        binding.urlInput.setSelection(url.length)
    }

    fun clear() {
        binding.urlInput.text.clear()
    }

    fun requestFocusOnInput() {
        binding.urlInput.requestFocus()
    }
}
