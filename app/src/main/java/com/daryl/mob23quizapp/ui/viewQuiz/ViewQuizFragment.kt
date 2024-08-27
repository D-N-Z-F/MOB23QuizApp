package com.daryl.mob23quizapp.ui.viewQuiz

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_2
import com.daryl.mob23quizapp.core.Constants.LOAD_DELAY_TIMING
import com.daryl.mob23quizapp.core.Constants.PARTICIPANTS
import com.daryl.mob23quizapp.core.Constants.QUESTIONS
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentViewQuizBinding
import com.daryl.mob23quizapp.ui.adapters.QuestionAdapter
import com.daryl.mob23quizapp.ui.adapters.StudentAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        binding?.mbSwitch?.apply {
            text = resourceProvider.getString(R.string.switch_view, PARTICIPANTS.capitalize())
            setOnClickListener { switchViews() }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.run {
            lifecycleScope.launch {
                quiz.collect {
                    setupQuizDetails(it)
                    questionAdapter.setQuestions(it.questions)
                    studentAdapter.setQuiz(it)
                    delay(LOAD_DELAY_TIMING)
                    dismissLoadingModal()
                }
            }
            lifecycleScope.launch {
                getAllStudents().collect {
                    delay(LOAD_DELAY_TIMING)
                    studentAdapter.setStudents(it)
                }
            }
        }
    }
    private fun switchViews() {
        // Switches between the questions and participants of the quiz.
        binding?.run {
            val isParticipants = mbSwitch.text.contains(PARTICIPANTS, ignoreCase = true)
            rvQuestions.visibility = invisible(isParticipants)
            rvParticipants.visibility = invisible(!isParticipants)
            val buttonText = when {
                isParticipants -> QUESTIONS
                else -> PARTICIPANTS
            }
            mbSwitch.text = resourceProvider.getString(
                R.string.switch_view, buttonText.capitalize()
            )
        }
    }
    private fun setupQuizDetails(quiz: Quiz) {
        // Sets up the necessary quiz details.
        binding?.run {
            tvID.text = quiz.id
            tvName.text = quiz.name
            tvCategory.text = quiz.category
            tvNoOfQuestions.text = quiz.questions.size.toString()
            tvTimeLimit.text = quiz.timePerQuestion.toString()
            llViewQuiz.visibility = invisible(quiz.questions.isEmpty())
            mbSettings.setOnClickListener {
                modalService.showEditQuizModal {
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
            rvQuestions.apply {
                adapter = questionAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            rvParticipants.apply {
                adapter = studentAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}