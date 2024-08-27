package com.daryl.mob23quizapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.ADD
import com.daryl.mob23quizapp.core.Constants.ERROR
import com.daryl.mob23quizapp.core.Constants.INFO
import com.daryl.mob23quizapp.core.Constants.LOAD_DELAY_TIMING
import com.daryl.mob23quizapp.core.Constants.SUCCESS
import com.daryl.mob23quizapp.core.services.ModalService
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.core.utils.Utils.popUpOptions
import com.daryl.mob23quizapp.data.models.Roles.TEACHER
import com.daryl.mob23quizapp.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseFragment<T: ViewDataBinding>: Fragment() {
    protected abstract val viewModel: BaseViewModel
    protected var binding: T? = null
    protected abstract fun getLayoutResource(): Int
    protected lateinit var modalService: ModalService
    private lateinit var loadingModal: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(getLayoutResource(), container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindView(view)
        onBindData(view)
    }
    protected open fun onBindView(view: View) {
        binding = DataBindingUtil.bind(view)
        modalService = ModalService(requireContext())
        loadingModal = modalService.createLoadingModal()
    }
    protected open fun onBindData(view: View) {
        viewModel.run {
            lifecycleScope.launch {
                signIn.collect {
                    delay(LOAD_DELAY_TIMING)
                    dismissLoadingModal()
                    val (string, role) = it
                    showSnackBar(view, string, INFO)
                    (requireActivity() as? MainActivity)?.setupNavViewMenu(role)
                    val destination = when(role) {
                        TEACHER -> R.id.teacherDashboardFragment
                        else -> R.id.studentHomeFragment
                    }
                    findNavController().navigate(
                        destination, null, popUpOptions(R.id.loginRegisterFragment, true)
                    )
                }
            }
            lifecycleScope.launch {
                error.collect {
                    delay(LOAD_DELAY_TIMING)
                    dismissLoadingModal()
                    showSnackBar(view, it, ERROR)
                }
            }
            lifecycleScope.launch {
                submit.collect {
                    delay(LOAD_DELAY_TIMING)
                    dismissLoadingModal()
                    showSnackBar(requireView(), it, SUCCESS)
                    if(it.contains(ADD.capitalize())) findNavController().popBackStack()
                }
            }
        }
    }
    protected fun invisible(condition: Boolean): Int = if(condition) View.GONE else View.VISIBLE

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun showSnackBar(view: View, message: String, type: String) {
        val backgroundTint = when(type) {
            INFO -> R.color.blue
            ERROR -> R.color.red
            SUCCESS -> R.color.green
            else -> R.color.grey
        }
        Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundTint))
            .show()
    }
    // Displays and hides the loading indicator whenever called.
    protected fun showLoadingModal() { if(!loadingModal.isShowing) loadingModal.show() }
    protected fun dismissLoadingModal() { if(loadingModal.isShowing) loadingModal.dismiss() }
}