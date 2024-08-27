package com.daryl.mob23quizapp.ui.studentHome

import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_2
import com.daryl.mob23quizapp.core.services.ModalService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentStudentHomeBinding
import com.daryl.mob23quizapp.ui.MainActivity
import com.daryl.mob23quizapp.ui.adapters.QuestionAdapter
import com.daryl.mob23quizapp.ui.adapters.QuizAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    private var timer: CountDownTimer? = null
    private var timeInMillis: Long? = null
    private var totalSeconds: Int? = null
    private var isPaused: Boolean = false
    private var isWaiting: Boolean = false
    private val answers = mutableListOf<String>()
    @Inject
    lateinit var resourceProvider: ResourceProvider
    private lateinit var adapter: QuestionAdapter
    override val viewModel: StudentHomeViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_student_home
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        binding?.run {
            mbStart.setOnClickListener {
                showLoadingModal()
                viewModel.getQuizById(etQuizID.text.toString())
            }
            mbSubmit.setOnClickListener {
                pauseTimer()
                ModalService(requireContext())
                    .showConfirmEndQuizModal({ resumeTimer() }) { endQuiz() }
            }
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                quiz?.let {
                    setupQuiz(it)
                    adapter.setQuestions(it.questions)
                    timeInMillis = totalSeconds!! * 1000L
                    displayQuiz(true)
                    delay(500)
                    dismissLoadingModal()
                    startTimer()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.result.collect {
                it?.let {
                    if(isWaiting) {
                        ModalService(requireContext()).showQuizResultModal(
                            resourceProvider, it.first, it.second, it.third
                        ) { displayQuiz(false) }
                        isWaiting = false
                    }
                }
            }
        }
    }
    private fun setupQuiz(quiz: Quiz) {
        totalSeconds = quiz.timePerQuestion * quiz.questions.size
        binding?.run {
            tvName.text = quiz.name
            tvTimeLimit.text = totalSeconds.toString()
        }
        quiz.questions.forEach { answers.add(it.answer) }
    }
    private fun startTimer() {
        timer = object: CountDownTimer(timeInMillis!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInMillis = millisUntilFinished
                updateTimer()
            }
            override fun onFinish() { endQuiz() }
        }
        timer?.start()
    }
    private fun endQuiz() {
        viewModel.run {
            isWaiting = true
            timer?.cancel()
            showLoadingModal()
            getCurrentQuiz()?.let { quiz ->
                val score = getScore(adapter.getAnswerList())
                val timeTaken =
                    (quiz.timePerQuestion * quiz.questions.size) - (timeInMillis!! / 1000L).toInt()
                updateParticipantList(quiz.id!!, score, timeTaken)
            }
        }
    }
    private fun updateTimer() {
        timeInMillis?.let {
            val secondsLeft = (it / 1000L).toInt()
            binding?.tvTimeLimit?.text = resourceProvider.getString(R.string.time_left, secondsLeft)
            binding?.cpiTimeLimit?.progress = ((secondsLeft.toFloat() / totalSeconds!!) * 100).toInt()
        }
    }
    private fun pauseTimer() {
        timer?.cancel()
        isPaused = true
    }
    private fun resumeTimer() {
        if(isPaused) {
            startTimer()
            isPaused = false
        }
    }
    private fun displayQuiz(bool: Boolean) {
        binding?.run {
            val toolbar = (requireActivity() as? MainActivity)?.findViewById<Toolbar>(R.id.toolbar)
            toolbar?.visibility = setVisibility(bool)
            llHome.visibility = setVisibility(bool)
            llQuiz.visibility = setVisibility(!bool)
        }
    }
    private fun setupAdapter() {
        adapter = QuestionAdapter(emptyList(), HOLDER_TYPE_2)

        binding?.rvQuestions?.adapter = adapter
        binding?.rvQuestions?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
    }
}