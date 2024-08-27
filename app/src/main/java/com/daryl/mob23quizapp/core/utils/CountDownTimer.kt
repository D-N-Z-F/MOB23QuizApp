package com.daryl.mob23quizapp.core.utils

import android.os.CountDownTimer
import com.daryl.mob23quizapp.core.Constants.COUNT_DOWN_INTERVAL

class CountDownTimer(
    private var milliseconds: Long,
    private val runOnTick: (Long) -> Unit,
    private val runOnFinish: () -> Unit
) {
    private var timer: CountDownTimer? = null
    private var isPaused = false
    fun start() {
        // Initialise the CountDownTimer object along with it's methods and start.
        timer = object: CountDownTimer(milliseconds, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                milliseconds = millisUntilFinished
                runOnTick(millisUntilFinished)
            }
            override fun onFinish() { runOnFinish() }
        }
        timer?.start()
    }
    fun pause() {
        timer?.cancel()
        isPaused = true
    }
    fun resume() {
        if(isPaused) {
            start()
            isPaused = false
        }
    }
    fun cancel() { timer?.cancel() }
}