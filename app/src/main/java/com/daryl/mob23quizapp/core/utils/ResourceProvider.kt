package com.daryl.mob23quizapp.core.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class ResourceProvider(
    private val context: Context
) {
    fun getString(@StringRes resId: Int, arg: String): String = context.getString(resId, arg)
    fun getColorList(@ColorRes resId: Int): ColorStateList = context.getColorStateList(resId)
}