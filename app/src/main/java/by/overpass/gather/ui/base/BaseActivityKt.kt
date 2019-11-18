package by.overpass.gather.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

abstract class BaseActivityKt<VM : ViewModel> : AppCompatActivity() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        inject()
        setupToolbar()
        viewModel = createViewModel()
        onBind()
    }

    protected abstract fun getLayoutRes(): Int

    protected abstract fun inject()

    protected abstract fun createViewModel(): VM

    protected open fun onActionBar(actionBar: ActionBar) {}

    protected open fun onBind() {}

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
        fun <A : BaseActivityKt<*>> start(
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
