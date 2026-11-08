package com.showfox.browser.ui.browser

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

class OnScreenKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var onKeyPress: ((String) -> Unit)? = null
    var onBackspace: (() -> Unit)? = null
    var onSpace: (() -> Unit)? = null
    var onEnter: (() -> Unit)? = null

    private val textRows = listOf(
        "1234567890",
        "qwertyuiop",
        "asdfghjkl",
        "zxcvbnm./:-_@"
    )

    init {
        orientation = VERTICAL
        setBackgroundColor(0xF2121212.toInt())
        val pad = dp(4)
        setPadding(pad, pad, pad, pad)
        buildKeyboard()
    }

    private fun buildKeyboard() {
        textRows.forEach { row -> addView(createRow(row.map { it.toString() })) }
        addView(createSpecialRow())
    }

    private fun createRow(labels: List<String>): View {
        val row = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
        }
        labels.forEach { label ->
            row.addView(createKey(label, 1f) { onKeyPress?.invoke(label) })
        }
        return row
    }

    private fun createSpecialRow(): View {
        val row = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
        }
        row.addView(createKey("\u232B", 2f) { onBackspace?.invoke() })
        row.addView(createKey("Space", 6f) { onSpace?.invoke() })
        row.addView(createKey("OK", 2f) { onEnter?.invoke() })
        return row
    }

    private fun createKey(label: String, weight: Float, onClick: () -> Unit): View {
        return Button(context).apply {
            text = label
            textSize = if (label.length > 1) 14f else 18f
            isAllCaps = false
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, weight).apply {
                setMargins(dp(2), dp(2), dp(2), dp(2))
            }
            setOnClickListener { onClick() }
        }
    }

    fun bind(editText: EditText, onDone: () -> Unit) {
        editText.showSoftInputOnFocus = false
        onKeyPress = { text -> insert(editText, text) }
        onBackspace = { backspace(editText) }
        onSpace = { insert(editText, " ") }
        onEnter = {
            visibility = View.GONE
            onDone()
        }
        editText.setOnClickListener { visibility = View.VISIBLE }
        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN &&
                (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                visibility = View.VISIBLE
                true
            } else {
                false
            }
        }
    }

    private fun insert(editText: EditText, text: String) {
        val start = editText.selectionStart.coerceAtLeast(0)
        val end = editText.selectionEnd.coerceAtLeast(0)
        val from = minOf(start, end)
        val to = maxOf(start, end)
        editText.text.replace(from, to, text)
        editText.setSelection(from + text.length)
    }

    private fun backspace(editText: EditText) {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start != end) {
            val from = minOf(start, end)
            val to = maxOf(start, end)
            editText.text.replace(from, to, "")
            editText.setSelection(from)
        } else if (start > 0) {
            editText.text.delete(start - 1, start)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
