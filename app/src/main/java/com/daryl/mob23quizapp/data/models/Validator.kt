package com.daryl.mob23quizapp.data.models

import com.daryl.mob23quizapp.core.Constants.PASSWORD
import com.daryl.mob23quizapp.core.Constants.PASSWORD_MISMATCH

data class Validator(
    val value: String,
    val regExp: String,
    val errorMessage: String
) {
    companion object {
        fun validate(vararg fields: Validator): String? {
            fields.forEach { field ->
                val value = field.value
                if(field.errorMessage.contains(PASSWORD)) {
                    val midIdx = value.length / 2
                    val password = value.substring(0, midIdx)
                    val password2 = value.substring(midIdx)
                    if(password != password2) return PASSWORD_MISMATCH
                }
                if(!Regex(field.regExp).matches(value)) return field.errorMessage
            }
            return null
        }
    }
}
