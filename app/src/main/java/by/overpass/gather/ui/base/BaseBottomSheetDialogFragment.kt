package by.overpass.gather.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<VM : ViewModel> : BottomSheetDialogFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

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
}