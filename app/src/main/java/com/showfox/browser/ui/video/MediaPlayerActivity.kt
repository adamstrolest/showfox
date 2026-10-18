package com.showfox.browser.ui.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.showfox.browser.R

class MediaPlayerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MediaPlayerActivity"
        private const val EXTRA_VIDEO_URL = "video_url"
        private const val EXTRA_VIDEO_TITLE = "video_title"
        private const val EXTRA_IS_DRM = "is_drm"

        fun createIntent(context: Context, url: String, title: String?, isDrm: Boolean = false): Intent {
            return Intent(context, MediaPlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_URL, url)
                putExtra(EXTRA_VIDEO_TITLE, title)
                putExtra(EXTRA_IS_DRM, isDrm)
            }
        }
    }

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private var controlsOverlay: VideoOverlayControls? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: run {
            Log.e(TAG, "No video URL provided")
            finish()
            return
        }
        val isDrm = intent.getBooleanExtra(EXTRA_IS_DRM, false)

        playerView = findViewById(R.id.player_view)
        controlsOverlay = findViewById(R.id.controls_overlay)

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        controlsOverlay?.attachPlayer(player)
        setupDpadControls()
    }

    private fun setupDpadControls() {
        playerView.setOnKeyListener { _, keyCode, event ->
            if (event.action != KeyEvent.ACTION_DOWN) return@setOnKeyListener false
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                    togglePlayPause()
                    true
                }
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    seekBackward()
                    true
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    seekForward()
                    true
                }
                KeyEvent.KEYCODE_DPAD_UP -> {
                    showControls()
                    true
                }
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    showControls()
                    true
                }
                KeyEvent.KEYCODE_BACK -> {
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        showControls()
    }

    private fun seekBackward() {
        val newPos = (player.currentPosition - 10_000).coerceAtLeast(0)
        player.seekTo(newPos)
        showControls()
    }

    private fun seekForward() {
        val newPos = (player.currentPosition + 10_000).coerceAtMost(player.duration)
        player.seekTo(newPos)
        showControls()
    }

    private fun showControls() {
        controlsOverlay?.show()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
