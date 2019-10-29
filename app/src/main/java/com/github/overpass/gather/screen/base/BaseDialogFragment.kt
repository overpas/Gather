package com.github.overpass.gather.screen.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject


abstract class BaseDialogFragment<VM : ViewModel> : DialogFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inject()
        viewModel = createViewModel()
        onBind()
    }

    protected abstract fun inject()

    protected abstract fun createViewModel(): VM

    protected open fun onBind() {}
}
