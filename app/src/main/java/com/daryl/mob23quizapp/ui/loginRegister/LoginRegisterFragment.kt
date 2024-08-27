package com.daryl.mob23quizapp.ui.loginRegister

import android.content.res.ColorStateList
import android.view.View
import androidx.fragment.app.viewModels
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.BLACK
import com.daryl.mob23quizapp.core.Constants.BLUE
import com.daryl.mob23quizapp.core.Constants.LOGIN
import com.daryl.mob23quizapp.core.Constants.REGISTER
import com.daryl.mob23quizapp.core.Constants.WHITE
import com.daryl.mob23quizapp.core.utils.ResourceProvider
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
    private var state: String = LOGIN
    private var colorList: Map<String, ColorStateList>? = null
    override fun onBindView(view: View) {
        super.onBindView(view)
        colorList = ResourceProvider(requireContext()).run {
            mapOf(
                BLUE to getColorList(R.color.blue),
                WHITE to getColorList(R.color.white),
                BLACK to getColorList(R.color.black)
            )
        }
        binding?.run {
            mbLogin.setOnClickListener {
                if(state == LOGIN) {
                    showLoadingModal()
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
                } else toggleState(it as MaterialButton)
            }
            mbRegister.setOnClickListener {
                if(state == REGISTER) {
                    showLoadingModal()
                    val role = Roles.valueOf(
                        spinnerRoles.selectedItem.toString().uppercase()
                    )
                    viewModel.register(
                        User(etUsername.text.toString(), etEmail.text.toString(), role),
                        etPassword.text.toString(),
                        etPassword2.text.toString()
                    )
                } else toggleState(it as MaterialButton)
            }
        }
    }
    private fun toggleState(clickedButton: MaterialButton) {
        // Toggles between login and register state.
        state = if(state == LOGIN) REGISTER else LOGIN
        resetInputs()
        changeButtonAppearance(clickedButton)
    }
    private fun resetInputs() {
        // Resets all inputs on state toggle.
        binding?.run {
            val inputBoxes = setOf(etUsername, etEmail, etPassword, etPassword2)
            inputBoxes.forEach {
                it.setText("")
                if(it.id == etUsername.id || it.id == etPassword2.id)
                    it.visibility = invisible(state == LOGIN)
            }
            spinnerRoles.setSelection(0)
            spinnerParent.visibility = invisible(state == LOGIN)
        }
    }
    private fun changeButtonAppearance(clickedButton: MaterialButton) {
        // Switches the button color to appear more interactive on state toggle.
        binding?.run {
            val buttons = setOf(mbLogin, mbRegister)
            buttons.forEach { button ->
                val isClicked = button.id == clickedButton.id
                colorList?.let { list ->
                    val backgroundColor = if(isClicked) BLUE else WHITE
                    val textColor = if(isClicked) WHITE else BLACK
                    button.backgroundTintList = list[backgroundColor]
                    button.setTextColor(list[textColor])
                }
            }
        }
    }
}