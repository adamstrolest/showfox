package com.showfox.browser.ui.browser

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.showfox.browser.databinding.ViewTopBarBinding

class TopBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTopBarBinding.inflate(LayoutInflater.from(context), this, true)

    fun updateNavigationState(canGoBack: Boolean, canGoForward: Boolean) {
        binding.btnBack.isEnabled = canGoBack
        binding.btnBack.alpha = if (canGoBack) 1.0f else 0.4f
        binding.btnForward.isEnabled = canGoForward
        binding.btnForward.alpha = if (canGoForward) 1.0f else 0.4f
    }
}
