package com.daryl.mob23quizapp.data.models

data class Validator(
    val value: String,
    val regExp: String,
    val errorMessage: String
) {
    companion object {
        fun validate(vararg fields: Validator): String? {
            fields.forEach { field ->
                val value = field.value
                if(field.errorMessage.contains("Password")) {
                    val midIdx = value.length / 2
                    val password = value.substring(0, midIdx)
                    val password2 = value.substring(midIdx)
                    if(password != password2) return "Both passwords must match!"
                }
                if(!Regex(field.regExp).matches(value)) return field.errorMessage
            }
            return null
        }
    }
}
