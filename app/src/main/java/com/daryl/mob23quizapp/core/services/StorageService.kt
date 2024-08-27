package com.daryl.mob23quizapp.core.services

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.daryl.mob23quizapp.data.models.Question
import java.io.BufferedReader
import java.io.InputStreamReader

class StorageService(
    private val context: Context
) {
    fun getFileName(uri: Uri): String? {
        // A cursor is an interface that gives read-write access to a result set returned by
        // a database query or a content provider.
        val cursor = context.contentResolver.query(
            uri, null, null, null
        )
        // Here we move the cursor to the first row which is where the file name is usually
        // located. We check if the the first row exists and use the column index get the file
        // name.
        cursor?.use {
            if(it.moveToFirst()) {
                val colIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return it.getString(colIdx)
            }
        }
        return null
    }
    // Here we open a stream of data associated to the URI, and then we use the buffered reader
    // that buffers the input stream characters for more efficient reading.
    fun parseCSVFile(uri: Uri): List<Question> =
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            extractContent(bufferedReader)
        } ?: emptyList()
    // Once we processed the data, we then get the values by splitting the comma-separated-values.
    private fun extractContent(bufferedReader: BufferedReader): List<Question> {
        val questions: MutableList<Question> = mutableListOf()
        var isFirstLine = true // Because my CSV file has a redundant first line.
        bufferedReader.forEachLine { line ->
            if(isFirstLine) isFirstLine = false
            else {
                val values = line.split(",")
                questions.add(
                    Question(
                        title = values[0],
                        options = values.subList(1, 5),
                        answer = values[5])
                )
            }
        }
        return questions.toList()
    }
}