package com.android_ai.csc13009.app.utils.extensions

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
//            val result = tts?.setLanguage(Locale.UK)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun estimateSpeechDuration(text: String): Int {
        val avgSpeedPerWord = 400 // Average milliseconds per word
        return (text.split(" ").size * avgSpeedPerWord).coerceAtLeast(1000) // Ensure at least 1s
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
