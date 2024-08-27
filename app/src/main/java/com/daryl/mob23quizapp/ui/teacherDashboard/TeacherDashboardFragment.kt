package com.daryl.mob23quizapp.ui.teacherDashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.CLIP_ID
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.databinding.FragmentTeacherDashboardBinding
import com.daryl.mob23quizapp.ui.adapters.QuizAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherDashboardFragment : BaseFragment<FragmentTeacherDashboardBinding>() {
    private lateinit var adapter: QuizAdapter
    private lateinit var clipboardManager: ClipboardManager
    override val viewModel: TeacherDashboardViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_teacher_dashboard
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding?.fabAdd?.setOnClickListener {
            findNavController().navigate(
                TeacherDashboardFragmentDirections.actionTeacherDashboardToAddQuiz()
            )
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getTeacherQuizzes().collect {
                adapter.setQuizzes(it)
                binding?.rvQuizzes?.visibility = invisible(it.isEmpty())
                binding?.tvEmpty?.visibility = invisible(it.isNotEmpty())
            }
        }
    }
    private fun copyToClipboard(id: String) {
        // Set copied text as clip data into the already initialised clipboard manager.
        val clipData = ClipData.newPlainText(CLIP_ID, id)
        clipboardManager.setPrimaryClip(clipData)
    }
    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList(), HOLDER_TYPE_1, null)
        adapter.listener = object : QuizAdapter.QuizListener {
            override fun onClickCopy(id: String) { copyToClipboard(id) }
            override fun onClickDelete(id: String) {
                showLoadingModal()
                viewModel.deleteQuiz(id)
            }
            override fun onClickItem(id: String) {
                findNavController().navigate(
                    TeacherDashboardFragmentDirections.actionTeacherDashboardToViewQuiz(id)
                )
            }
        }

        binding?.rvQuizzes?.adapter = adapter
        binding?.rvQuizzes?.layoutManager = LinearLayoutManager(requireContext())
    }
}