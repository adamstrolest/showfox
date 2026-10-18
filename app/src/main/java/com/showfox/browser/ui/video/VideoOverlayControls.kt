package com.showfox.browser.ui.video

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.showfox.browser.databinding.ViewVideoControlsBinding

@UnstableApi
class VideoOverlayControls @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewVideoControlsBinding.inflate(LayoutInflater.from(context), this, true)
    private var player: Player? = null
    private val hideHandler = Handler(Looper.getMainLooper())
    private val hideRunnable = Runnable { hide() }
    private var isShowing = false

    fun attachPlayer(player: Player) {
        this.player = player
        binding.btnPlayPause.setOnClickListener {
            if (player.isPlaying) player.pause() else player.play()
        }
    }

    fun show() {
        isShowing = true
        visibility = View.VISIBLE
        scheduleHide()
    }

    fun hide() {
        isShowing = false
        visibility = View.GONE
        hideHandler.removeCallbacks(hideRunnable)
    }

    private fun scheduleHide() {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, 5000)
    }
}
