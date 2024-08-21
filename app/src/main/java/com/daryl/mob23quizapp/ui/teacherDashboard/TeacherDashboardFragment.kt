package com.daryl.mob23quizapp.ui.teacherDashboard

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.FragmentTeacherDashboardBinding
import com.daryl.mob23quizapp.ui.adapters.QuizAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherDashboardFragment : BaseFragment<FragmentTeacherDashboardBinding>() {
    private lateinit var adapter: QuizAdapter
    override val viewModel: TeacherDashboardViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_teacher_dashboard
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        binding?.fabAdd?.setOnClickListener {
            findNavController().navigate(
                TeacherDashboardFragmentDirections.actionTeacherDashboardToAddQuiz()
            )
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getAllQuizzes().collect {
                adapter.setQuizzes(it)
                binding?.rvQuizzes?.visibility = setVisibility(it.isEmpty())
                binding?.tvEmpty?.visibility = setVisibility(it.isNotEmpty())
            }
        }
    }
    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList())

        binding?.rvQuizzes?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}