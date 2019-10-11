package com.github.overpass.gather.screen.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.github.overpass.gather.di.ComponentManager
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel, C> : AppCompatActivity() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    protected abstract val componentManager: ComponentManager<C>

    protected abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        onComponentCreated(componentManager.get())
        ButterKnife.bind(this)
        setupToolbar()
        viewModel = createViewModel()
        onBind()
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            clearComponent()
        }
    }

    protected abstract fun onComponentCreated(component: C)

    protected abstract fun createViewModel(): VM

    protected open fun onActionBar(actionBar: ActionBar) {}

    protected open fun onBind() {}

    protected open fun clearComponent() {
        componentManager.clear()
    }

    // TODO: Use non-default toolbar
    protected open fun setupToolbar() {
        supportActionBar?.apply {
            title = TITLE_EMPTY
            setDisplayHomeAsUpEnabled(true)
            onActionBar(this)
        }
    }

    companion object {

        private const val TITLE_EMPTY = ""

        @JvmStatic
        fun <A : BaseActivity<*, *>> start(
                context: Context,
                activityClass: Class<A>,
                key: String,
                value: String
        ) {
            val intent = Intent(context, activityClass)
            intent.putExtra(key, value)
            context.startActivity(intent)
        }
    }
}
