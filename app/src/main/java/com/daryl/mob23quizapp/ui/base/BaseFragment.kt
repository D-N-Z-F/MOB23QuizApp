package com.daryl.mob23quizapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.ERROR
import com.daryl.mob23quizapp.core.Constants.INFO
import com.daryl.mob23quizapp.core.Constants.SUCCESS
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<T: ViewDataBinding>: Fragment() {
    protected abstract val viewModel: BaseViewModel
    protected var binding: T? = null
    protected abstract fun getLayoutResource(): Int

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
    }

    protected open fun onBindData(view: View) {

    }

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
}