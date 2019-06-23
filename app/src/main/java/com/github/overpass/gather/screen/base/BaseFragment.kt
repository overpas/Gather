package com.github.overpass.gather.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

import butterknife.ButterKnife

abstract class BaseFragment<VM : ViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    protected abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutRes, container, false)
        // TODO: Remove Butterknife
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = createViewModel()
        onBind()
    }

    protected abstract fun createViewModel(): VM

    protected open fun onBind() {}

    companion object {

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
