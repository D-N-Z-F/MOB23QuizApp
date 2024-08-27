package com.daryl.mob23quizapp.ui.studentHome

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.COUNT_DOWN_INTERVAL
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_2
import com.daryl.mob23quizapp.core.Constants.LOAD_DELAY_TIMING
import com.daryl.mob23quizapp.core.utils.CountDownTimer
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentStudentHomeBinding
import com.daryl.mob23quizapp.ui.MainActivity
import com.daryl.mob23quizapp.ui.adapters.QuestionAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    @Inject
    lateinit var resourceProvider: ResourceProvider
    private lateinit var adapter: QuestionAdapter
    private var timer: CountDownTimer? = null
    private var milliseconds: Long? = null
    private var totalSeconds: Int? = null
    private var isWaiting: Boolean = false
    private val correctAnswers = mutableListOf<String>()

    override val viewModel: StudentHomeViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_student_home
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        binding?.run {
            mbStart.setOnClickListener {
                showLoadingModal()
                val id = etQuizID.text.toString()
                viewModel.getQuizById(id)
            }
            mbSubmit.setOnClickListener {
                timer?.pause()
                modalService.showConfirmEndQuizModal({ timer?.resume() }) { endQuiz() }
            }
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.run {
            lifecycleScope.launch {
                quiz.collect { quiz ->
                    quiz?.let {
                        if(timer != null) timer?.cancel()
                        adapter.setQuestions(it.questions)
                        setupQuiz(it)
                        delay(LOAD_DELAY_TIMING)
                        displayQuiz(true)
                        dismissLoadingModal()
                        setupAndStartTimer()
                    }
                }
            }
            lifecycleScope.launch {
                result.collect { result ->
                    result?.let {
                        if(isWaiting) {
                            modalService.showQuizResultModal(resourceProvider, it) {
                                displayQuiz(false)
                            }
                            isWaiting = false
                        }
                    }
                }
            }
        }
    }
    private fun displayQuiz(bool: Boolean) {
        // Switches between displaying the quiz and home screen.
        binding?.run {
            val toolbar = (requireActivity() as? MainActivity)?.findViewById<Toolbar>(R.id.toolbar)
            toolbar?.visibility = invisible(bool)
            llHome.visibility = invisible(bool)
            llQuiz.visibility = invisible(!bool)
        }
    }
    private fun setupQuiz(quiz: Quiz) {
        // Stores info needed for the timer and sets up quiz details along with the answers.
        totalSeconds = quiz.timePerQuestion * quiz.questions.size
        milliseconds = totalSeconds!! * COUNT_DOWN_INTERVAL
        binding?.tvName?.text = quiz.name
        binding?.tvTimeLimit?.text = totalSeconds.toString()
        quiz.questions.forEach { correctAnswers.add(it.answer) }
    }
    private fun endQuiz() {
        // Stops the timer, shows loading indicator, and updates the quiz's participant list.
        viewModel.run {
            timer?.cancel()
            isWaiting = true
            showLoadingModal()
            getQuizId()?.let {
                val answers = adapter.getAnswerList()
                val score = getScore(answers, correctAnswers)
                val timeTaken = totalSeconds!! - (milliseconds!! / COUNT_DOWN_INTERVAL).toInt()
                updateQuiz(it, score, timeTaken)
            }
        }
    }
    private fun setupAndStartTimer() {
        // Initialises the timer and starts counting.
        timer = CountDownTimer(milliseconds!!, { updateTimer(it) }) { endQuiz() }
        timer?.start()
    }
    private fun updateTimer(millisUntilFinished: Long) {
        // On each tick, update the UI that displays the timer.
        milliseconds = millisUntilFinished
        val seconds = (milliseconds!! / COUNT_DOWN_INTERVAL).toInt()
        val hundredPercent = 100
        val currentPercentage = ((seconds.toFloat() / totalSeconds!!) * hundredPercent).toInt()
        binding?.tvTimeLimit?.text = resourceProvider.getString(R.string.time_left, seconds)
        binding?.cpiTimeLimit?.progress = currentPercentage
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