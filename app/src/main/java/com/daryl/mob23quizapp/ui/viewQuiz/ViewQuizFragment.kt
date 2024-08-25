package com.daryl.mob23quizapp.ui.viewQuiz

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.services.ModalService
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentViewQuizBinding
import com.daryl.mob23quizapp.ui.adapters.QuestionAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewQuizFragment : BaseFragment<FragmentViewQuizBinding>() {
    private lateinit var adapter: QuestionAdapter
    private val args: ViewQuizFragmentArgs by navArgs()
    override val viewModel: ViewQuizViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_view_quiz
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getQuizById(args.quizId)
            viewModel.quiz.collect {
                setupQuizDetails(it)
                adapter.setQuestions(it.questions)
            }
        }
    }
    private fun setupQuizDetails(quiz: Quiz) {
        binding?.run {
            tvID.text = quiz.id
            tvName.text = quiz.name
            tvCategory.text = quiz.category
            tvNoOfQuestions.text = quiz.questions.size.toString()
            tvTimeLimit.text = quiz.timePerQuestion.toString()
            mbSettings.setOnClickListener {
                ModalService(requireContext()).modalEditQuiz {
                    viewModel.updateQuiz(quiz.copy(timePerQuestion = it))
                }
            }
        }
    }
    private fun setupAdapter() {
        adapter = QuestionAdapter(emptyList())

        binding?.rvQuestions?.adapter = adapter
        binding?.rvQuestions?.layoutManager = LinearLayoutManager(requireContext())
    }
}