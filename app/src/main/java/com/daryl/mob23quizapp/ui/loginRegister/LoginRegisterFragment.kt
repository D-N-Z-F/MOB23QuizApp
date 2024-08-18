package com.daryl.mob23quizapp.ui.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.databinding.FragmentLoginRegisterBinding
import com.daryl.mob23quizapp.ui.base.BaseFragment

class LoginRegisterFragment : BaseFragment<FragmentLoginRegisterBinding>() {
    override val viewModel: LoginRegisterViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_login_register
}