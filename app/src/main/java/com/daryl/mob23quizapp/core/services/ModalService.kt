package com.daryl.mob23quizapp.core.services

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.daryl.mob23quizapp.databinding.LayoutEditQuizModalBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class ModalService @Inject constructor(
    private val context: Context
) {
    fun modalEditQuiz(onSuccess: (Int) -> Unit) {
        val view = LayoutEditQuizModalBinding.inflate(LayoutInflater.from(context))
        val dialog = MaterialAlertDialogBuilder(context).setView(view.root).create()
        view.mbAdd.setOnClickListener {
            val timeLimit = view.etTimeLimit.text.toString().toIntOrNull() ?: 10
            onSuccess(timeLimit)
            dialog.dismiss()
        }
        dialog.show()
    }
}