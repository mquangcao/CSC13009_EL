package com.android_ai.csc13009.app.utils.extensions;

import android.content.Context;
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton;
import android.widget.ProgressBar;

class TTSSetter() {
    fun setTTS(audioButton:ImageButton, audioProgress: ProgressBar, word: String, context:Context) {
        val tts = TTSHelper(context)
        val handler = Handler(Looper.getMainLooper())
        var updateProgress: Runnable? = null

        val estimatedDuration = tts.estimateSpeechDuration(word)
        val updateInterval = 100 // Update every 100ms

        audioButton.setOnClickListener {
            // reset tien do doc
            tts.stop()
            updateProgress?.let { handler.removeCallbacks(it) }
            audioProgress.progress = 0

            // phat am thanh
            tts.speak(word)

            val startTime = System.currentTimeMillis()

            updateProgress = object : Runnable {
                override fun run() {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val progress = ((elapsedTime.toFloat() / estimatedDuration) * 100).toInt()
                    audioProgress.progress = progress

                    if (elapsedTime < estimatedDuration) {
                        handler.postDelayed(this, updateInterval.toLong())
                    } else {
                        audioProgress.progress = 100
                    }
                }
            }

            handler.post(updateProgress as Runnable)
        }
    }
}
