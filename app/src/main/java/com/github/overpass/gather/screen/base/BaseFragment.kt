package com.github.overpass.gather.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import butterknife.ButterKnife
import com.github.overpass.gather.di.ComponentManager
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel, C> : Fragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected abstract val layoutRes: Int

    protected abstract val componentManager: ComponentManager<*, C>

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    protected val parentViewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory)

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

    companion object {

        @JvmStatic
        fun <F : Fragment> newInstance(fragment: F, stringArgs: Map<String, String>): F {
            val arguments = Bundle()
            for ((key, value) in stringArgs) {
                arguments.putString(key, value)
            }
            fragment.arguments = arguments
            return fragment
        }
    }
}
