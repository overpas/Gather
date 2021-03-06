package by.overpass.gather.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import javax.inject.Inject

abstract class BaseFragmentKt<VM : ViewModel> : Fragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    protected val parentViewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inject()
        viewModel = createViewModel()
        onBind()
    }

    protected abstract fun getLayoutRes(): Int

    protected abstract fun inject()

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
