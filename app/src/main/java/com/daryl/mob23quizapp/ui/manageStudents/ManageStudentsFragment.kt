package com.daryl.mob23quizapp.ui.manageStudents

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.databinding.FragmentManageStudentsBinding
import com.daryl.mob23quizapp.ui.adapters.StudentAdapter
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageStudentsFragment : BaseFragment<FragmentManageStudentsBinding>() {
    private lateinit var adapter: StudentAdapter
    override val viewModel: ManageStudentsViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_manage_students
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getAllStudents().collect {
                adapter.setStudents(it)
                binding?.rvStudents?.visibility = invisible(it.isEmpty())
                binding?.tvEmpty?.visibility = invisible(it.isNotEmpty())
            }
        }
    }
    private fun setupAdapter() {
        adapter = StudentAdapter(emptyList(), HOLDER_TYPE_1, null)

        binding?.rvStudents?.adapter = adapter
        binding?.rvStudents?.layoutManager = LinearLayoutManager(requireContext())
    }
}