package com.showfox.browser.voice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.Locale

class VoiceSearchManager(private val activity: Activity) {

    companion object {
        private const val TAG = "VoiceSearchManager"
        const val VOICE_REQUEST_CODE = 1001
    }

    fun isVoiceRecognitionAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(activity)
    }

    fun startVoiceSearch() {
        if (!isVoiceRecognitionAvailable()) {
            Log.w(TAG, "Voice recognition not available on this device")
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            activity.startActivityForResult(intent, VOICE_REQUEST_CODE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start voice recognition", e)
        }
    }
}
