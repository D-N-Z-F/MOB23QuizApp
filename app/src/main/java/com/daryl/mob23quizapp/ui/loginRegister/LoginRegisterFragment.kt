package com.daryl.mob23quizapp.ui.loginRegister

import android.content.res.ColorStateList
import android.view.View
import androidx.fragment.app.viewModels
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.AuthState
import com.daryl.mob23quizapp.data.models.AuthState.LOGIN
import com.daryl.mob23quizapp.data.models.AuthState.REGISTER
import com.daryl.mob23quizapp.data.models.Roles
import com.daryl.mob23quizapp.data.models.User
import com.daryl.mob23quizapp.databinding.FragmentLoginRegisterBinding
import com.daryl.mob23quizapp.ui.base.BaseFragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterFragment : BaseFragment<FragmentLoginRegisterBinding>() {
    override val viewModel: LoginRegisterViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_login_register
    private var state: AuthState = LOGIN
    private var colorList: Map<String, ColorStateList>? = null
    override fun onBindView(view: View) {
        super.onBindView(view)
        colorList = ResourceProvider(requireContext()).run {
            mapOf(
                "Blue" to getColorList(R.color.blue),
                "White" to getColorList(R.color.white),
                "Black" to getColorList(R.color.black)
            )
        }
        binding?.run {
            mbLogin.setOnClickListener {
                if(state == LOGIN) {
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
                } else toggleState(it as MaterialButton)
            }
            mbRegister.setOnClickListener {
                if(state == REGISTER) {
                    val role = when(spinnerRoles.selectedItem.toString()) {
                        "Teacher" -> Roles.TEACHER
                        else -> Roles.STUDENT
                    }
                    viewModel.register(
                        User(etUsername.text.toString(), etEmail.text.toString(), role),
                        etPassword.text.toString(),
                        etPassword2.text.toString()
                    )
                } else toggleState(it as MaterialButton)
            }
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
    }
    private fun toggleState(clickedButton: MaterialButton) {
        state = if(state == LOGIN) REGISTER else LOGIN
        resetInputs()
        changeButtonAppearance(clickedButton)
    }
    private fun resetInputs() {
        binding?.run {
            val inputBoxes = setOf(etUsername, etEmail, etPassword, etPassword2)
            inputBoxes.forEach {
                it.setText("")
                if(it.id == etUsername.id || it.id == etPassword2.id) {
                    it.visibility = setVisibility()
                }
            }
            spinnerRoles.setSelection(0)
            spinnerParent.visibility = setVisibility()
        }
    }
    private fun changeButtonAppearance(clickedButton: MaterialButton) {
        binding?.run {
            val buttons = setOf(mbLogin, mbRegister)
            buttons.forEach { button ->
                val isClicked = button.id == clickedButton.id
                colorList?.let { list ->
                    val backgroundColor = if(isClicked) "Blue" else "White"
                    val textColor = if(isClicked) "White" else "Black"
                    button.backgroundTintList = list[backgroundColor]
                    button.setTextColor(list[textColor])
                }
            }
        }
    }
    private fun setVisibility(): Int = if(state == LOGIN) View.GONE else View.VISIBLE
}