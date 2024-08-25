package com.daryl.mob23quizapp.ui.studentHome

import androidx.fragment.app.viewModels
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.databinding.FragmentStudentHomeBinding
import com.daryl.mob23quizapp.ui.base.BaseFragment
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    override val viewModel: StudentHomeViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_student_home
}