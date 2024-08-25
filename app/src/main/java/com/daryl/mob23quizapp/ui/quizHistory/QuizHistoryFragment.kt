package com.daryl.mob23quizapp.ui.quizHistory

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_2
import com.daryl.mob23quizapp.databinding.FragmentQuizHistoryBinding
import com.daryl.mob23quizapp.ui.adapters.QuizAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizHistoryFragment : BaseFragment<FragmentQuizHistoryBinding>() {
    private lateinit var adapter: QuizAdapter
    override val viewModel: QuizHistoryViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_quiz_history
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getStudentQuizHistory().collect {
                adapter.setQuizzes(it)
                binding?.rvQuizzes?.visibility = setVisibility(it.isEmpty())
                binding?.tvEmpty?.visibility = setVisibility(it.isNotEmpty())
            }
        }
    }
    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList(), HOLDER_TYPE_2)

        binding?.rvQuizzes?.adapter = adapter
        binding?.rvQuizzes?.layoutManager = LinearLayoutManager(requireContext())
    }
}