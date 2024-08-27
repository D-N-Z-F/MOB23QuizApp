package com.daryl.mob23quizapp.core.services

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.Results
import com.daryl.mob23quizapp.databinding.LayoutConfirmEndQuizModalBinding
import com.daryl.mob23quizapp.databinding.LayoutEditQuizModalBinding
import com.daryl.mob23quizapp.databinding.LayoutLoadingModalBinding
import com.daryl.mob23quizapp.databinding.LayoutQuizResultModalBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ModalService(
    private val context: Context
) {
    fun showQuizResultModal(
        resourceProvider: ResourceProvider, results: Results, onClick: () -> Unit
    ) {
        val (maxScore, score, timeTaken) = results
        val view = LayoutQuizResultModalBinding.inflate(LayoutInflater.from(context))
        val dialog = dialogBuilder(view).setCancelable(false).create()
        view.run {
            tvScore.text = resourceProvider.getString(R.string.your_score, score, maxScore)
            tvTimeTaken.text = resourceProvider.getString(R.string.time_taken, timeTaken)
            mbBack.setOnClickListener {
                onClick()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    fun showConfirmEndQuizModal(onCancel: () -> Unit, onConfirm: () -> Unit) {
        val view = LayoutConfirmEndQuizModalBinding.inflate(LayoutInflater.from(context))
        val dialog = dialogBuilder(view).setCancelable(false).create()
        view.mbYes.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }
        view.mbNo.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }
        dialog.window?.setDimAmount(1f)
        dialog.show()
    }
    fun showEditQuizModal(onSuccess: (Int) -> Unit) {
        val view = LayoutEditQuizModalBinding.inflate(LayoutInflater.from(context))
        val dialog = dialogBuilder(view).create()
        view.mbAdd.setOnClickListener {
            val timeLimit = view.etTimeLimit.text.toString().toIntOrNull() ?: 10
            onSuccess(timeLimit)
            dialog.dismiss()
        }
        dialog.show()
    }
    fun createLoadingModal() = dialogBuilder(
        LayoutLoadingModalBinding.inflate(LayoutInflater.from(context))
    ).setCancelable(false).create()

    private fun dialogBuilder(view: ViewDataBinding): AlertDialog.Builder =
        MaterialAlertDialogBuilder(context).setView(view.root)
}