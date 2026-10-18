package com.showfox.browser.ui.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.showfox.browser.R

class MediaPlayerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MediaPlayerActivity"
        private const val EXTRA_VIDEO_URL = "video_url"
        private const val EXTRA_IS_DRM = "is_drm"

        fun createIntent(context: Context, url: String, isDrm: Boolean = false): Intent {
            return Intent(context, MediaPlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_URL, url)
                putExtra(EXTRA_IS_DRM, isDrm)
            }
        }
    }

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: run {
            Log.e(TAG, "No video URL provided")
            finish()
            return
        }

        playerView = findViewById(R.id.player_view)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
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
