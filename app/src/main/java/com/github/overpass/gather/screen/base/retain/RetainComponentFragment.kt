package com.github.overpass.gather.screen.base.retain

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.github.overpass.gather.screen.base.ViewModelFactory
import javax.inject.Inject

abstract class RetainComponentFragment<VM : ViewModel, C> : Fragment() {

    abstract val theTag: String

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    protected val parentViewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory)

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var viewModel: VM

    protected var componentContainerProvider: ComponentContainerProvider? = null
    protected var component: C? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ComponentContainerProvider) {
            componentContainerProvider = context
        } else {
            throw IllegalStateException("$context must be an instance of ${ComponentContainerProvider::class.simpleName}")
        }
        component = componentContainerProvider!!.componentContainer
                .getByTag(theTag)
                ?.let { it as C? }
                ?: createComponent()
        onComponentCreated(component!!)
    }

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
        viewModel = createViewModel()
        onBind()
    }

    override fun onDetach() {
        super.onDetach()
        if (isRemoving) {
            componentContainerProvider!!.componentContainer.removeByTag(theTag)
        } else {
            componentContainerProvider!!.componentContainer.putWithTag(theTag, component!!)
        }
        componentContainerProvider = null
    }

    protected abstract fun createComponent(): C

    protected abstract fun onComponentCreated(component: C)

    protected abstract fun createViewModel(): VM

    protected open fun onBind() {}

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
