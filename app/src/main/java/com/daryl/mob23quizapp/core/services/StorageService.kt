package com.daryl.mob23quizapp.core.services

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Question
import java.io.BufferedReader
import java.io.InputStreamReader

class StorageService(
    private val context: Context
) {
    fun getFileName(uri: Uri): String? {
        val cursor = context.contentResolver.query(
            uri, null, null, null
        )
        cursor?.use {
            if(it.moveToFirst()) {
                val colIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return it.getString(colIdx)
            }
        }
        return null
    }
    fun parseCSVFile(uri: Uri): List<Question> =
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            extractContent(bufferedReader)
        } ?: emptyList()

    private fun extractContent(bufferedReader: BufferedReader): List<Question> {
        val questions: MutableList<Question> = mutableListOf()
        var isFirstLine = true // Because my CSV file has a redundant first line.
        bufferedReader.forEachLine { line ->
            if(isFirstLine) isFirstLine = false
            else {
                val values = line.split(",")
                questions.add(
                    Question(title = values[0], options = values.subList(1, 5), answer = values[5])
                )
            }
        }
        return questions.toList()
    }
}