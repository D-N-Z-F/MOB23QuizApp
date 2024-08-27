package com.daryl.mob23quizapp.ui.viewQuiz

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_2
import com.daryl.mob23quizapp.core.Constants.PARTICIPANTS
import com.daryl.mob23quizapp.core.Constants.QUESTIONS
import com.daryl.mob23quizapp.core.services.ModalService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentViewQuizBinding
import com.daryl.mob23quizapp.ui.adapters.QuestionAdapter
import com.daryl.mob23quizapp.ui.adapters.StudentAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewQuizFragment : BaseFragment<FragmentViewQuizBinding>() {
    @Inject
    lateinit var resourceProvider: ResourceProvider
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var studentAdapter: StudentAdapter
    override val viewModel: ViewQuizViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_view_quiz
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        showLoadingModal()
        binding?.run {
            mbSwitch.text = resourceProvider.getString(R.string.see, PARTICIPANTS.capitalize())
            mbSwitch.setOnClickListener { switchViews() }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.quiz.collect {
                setupQuizDetails(it)
                questionAdapter.setQuestions(it.questions)
                studentAdapter.setQuiz(it)
                binding?.llViewQuiz?.visibility = setVisibility(it.questions.isEmpty())
                delay(500)
                dismissLoadingModal()
            }
        }
        lifecycleScope.launch {
            viewModel.getAllStudents().collect {
                delay(500)
                studentAdapter.setStudents(it)
            }
        }
    }
    private fun switchViews() {
        binding?.run {
            val bool = mbSwitch.text.contains(PARTICIPANTS, ignoreCase = true)
            rvQuestions.visibility = setVisibility(bool)
            rvParticipants.visibility = setVisibility(!bool)
            mbSwitch.text = resourceProvider.getString(
                R.string.see,
                when {
                    bool -> QUESTIONS.capitalize()
                    else -> PARTICIPANTS.capitalize()
                }
            )
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
                ModalService(requireContext()).showEditQuizModal {
                    showLoadingModal()
                    viewModel.updateQuiz(quiz.copy(timePerQuestion = it))
                }
            }
        }
    }
    private fun setupAdapter() {
        questionAdapter = QuestionAdapter(emptyList(), HOLDER_TYPE_1)
        studentAdapter = StudentAdapter(emptyList(), HOLDER_TYPE_2, resourceProvider)

        binding?.run {
            rvQuestions.adapter = questionAdapter
            rvQuestions.layoutManager = LinearLayoutManager(requireContext())
            rvParticipants.adapter = studentAdapter
            rvParticipants.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}