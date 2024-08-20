package com.daryl.mob23quizapp.ui.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentHomeBinding
import com.daryl.mob23quizapp.ui.adapters.QuizAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: QuizAdapter
    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_home
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
    }

    private fun setupAdapter() {
        adapter = QuizAdapter(
            listOf(
                Quiz(
                    id = 123,
                    name = "Science Quiz",
                    category = "Science",
                    questions = listOf(Question(id = 1, title = "haa", options = emptyList(), answer = "wow"), Question(id = 1, title = "haa", options = emptyList(), answer = "wow"), Question(id = 1, title = "haa", options = emptyList(), answer = "wow")),
                    timePerQuestion = 10
                )
            )
        )

        binding?.rvQuizzes?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}