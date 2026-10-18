package com.showfox.browser.ui.video

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@UnstableApi
class ExoPlayerController(private val context: Context) {

    companion object {
        private const val TAG = "ExoPlayerController"
    }

    private var player: ExoPlayer? = null

    fun createPlayer(): ExoPlayer {
        releasePlayer()
        player = ExoPlayer.Builder(context).build()
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    Log.d(TAG, "Player ready")
                }
            }
        })
        return player!!
    }

    fun loadUrl(url: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    fun releasePlayer() {
        player?.release()
        player = null
    }

    fun getPlayer(): ExoPlayer? = player
}
