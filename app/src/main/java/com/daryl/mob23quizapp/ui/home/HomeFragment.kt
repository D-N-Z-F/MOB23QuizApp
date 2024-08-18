package com.daryl.mob23quizapp.ui.home

import androidx.fragment.app.viewModels
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.databinding.FragmentHomeBinding
import com.daryl.mob23quizapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_home
}