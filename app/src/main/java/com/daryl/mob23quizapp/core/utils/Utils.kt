package com.daryl.mob23quizapp.core.utils

import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavOptions

object Utils {
    fun debugLog(): (Any) -> Unit = { Log.d("debugging", it.toString()) }
    fun String.capitalize() =
        this.substring(0, 1).uppercase() + this.substring(1).lowercase()
    fun popUpOptions(@IdRes destId: Int, bool: Boolean): NavOptions =
        NavOptions.Builder().setPopUpTo(destId, bool).setLaunchSingleTop(bool).build()
}