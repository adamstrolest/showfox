package com.showfox.browser.ui.browser

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.showfox.browser.R

class OnScreenKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    var onKeyPress: ((String) -> Unit)? = null
    var onBackspace: (() -> Unit)? = null
    var onEnter: (() -> Unit)? = null
    var onSpace: (() -> Unit)? = null

    init {
        columnCount = 10
        buildKeyboard()
    }

    private fun buildKeyboard() {
        val rows = listOf(
            "1234567890",
            "AZERTYUIOP",
            "QSDFGHJKLM",
            "WXCVBN .,/?:@"
        )

        for (row in rows) {
            for (ch in row) {
                addView(createKeyButton(ch.toString()))
            }
        }

        addView(createSpecialKey("⌫", width = 150) { onBackspace?.invoke() })
        addView(createSpecialKey("Espace", width = 300) { onSpace?.invoke() })
        addView(createSpecialKey("OK", width = 150) { onEnter?.invoke() })
    }

    private fun createKeyButton(label: String): View {
        return Button(context).apply {
            text = label
            textSize = 18f
            setOnClickListener { onKeyPress?.invoke(label) }
            layoutParams = LayoutParams().apply {
                width = 80
                height = 80
                setMargins(4, 4, 4, 4)
            }
        }
    }

    private fun createSpecialKey(label: String, width: Int, onClick: () -> Unit): View {
        return Button(context).apply {
            text = label
            textSize = 16f
            setOnClickListener { onClick() }
            layoutParams = LayoutParams().apply {
                this.width = width
                height = 80
                setMargins(4, 4, 4, 4)
            }
        }
    }
}
