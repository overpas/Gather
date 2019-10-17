package com.github.overpass.gather.screen.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.overpass.gather.di.ComponentManager
import javax.inject.Inject


abstract class BaseDialogFragment<VM : ViewModel, C> : DialogFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected abstract val componentManager: ComponentManager<*, C>

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onComponentCreated(createComponent())
        viewModel = createViewModel()
        onBind()
    }

    override fun onDetach() {
        super.onDetach()
        if (isRemoving) {
            clearComponent()
        }
    }

    protected abstract fun createComponent(): C

    protected abstract fun onComponentCreated(component: C)

    protected abstract fun createViewModel(): VM

    protected open fun onBind() {}

    protected open fun clearComponent() {
        componentManager.clear()
    }
}