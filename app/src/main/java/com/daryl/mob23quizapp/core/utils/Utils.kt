package com.daryl.mob23quizapp.core.utils

import android.util.Log

object Utils {


    // Temporary for debugging
    fun debugLog(): (Any) -> Unit = { Log.d("debugging", it.toString()) }
}