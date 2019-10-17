package com.github.overpass.gather.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.github.overpass.gather.di.ComponentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<VM : ViewModel, C> : BottomSheetDialogFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    protected abstract val layoutRes: Int

    protected abstract val componentManager: ComponentManager<*, C>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutRes, container, false)
        // TODO: Remove Butterknife
        ButterKnife.bind(this, view)
        return view
    }

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