package com.daryl.mob23quizapp.ui.addQuiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentAddQuizBinding
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddQuizFragment : BaseFragment<FragmentAddQuizBinding>() {
    private lateinit var resultLauncher: ActivityResultLauncher<Array<String>>
    override val viewModel: AddQuizViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_add_quiz
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        resultLauncher = setupResultLauncher()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.run {
            mbUpload.setOnClickListener {
                resultLauncher.launch(arrayOf("text/comma-separated-values"))
            }
            mbAdd.setOnClickListener {
                showLoadingModal()
                viewModel.add(
                    Quiz(
                        name = etName.text.toString(),
                        category = etCategory.text.toString(),
                        questions = emptyList(),
                        timePerQuestion = etTimeLimit.text.toString().toIntOrNull() ?: 10
                    )
                )
            }
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
    }
    private fun setupResultLauncher(): ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                viewModel.getQuestionsFromCSV(it)
                binding?.etUpload?.hint = viewModel.getFileName(it)
            }
        }
}