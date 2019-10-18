package com.github.overpass.gather.screen.base.retain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.github.overpass.gather.screen.base.ViewModelFactory
import javax.inject.Inject

abstract class RetainComponentActivity<VM : ViewModel, C> : AppCompatActivity() {

    protected val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this, viewModelFactory)

    protected abstract val layoutRes: Int

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    private var componentContainer = ActivityComponentContainer(mutableMapOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        componentContainer = lastCustomNonConfigurationInstance as ActivityComponentContainer
        if (componentContainer.activityComponent == null) {
            componentContainer.activityComponent = createComponent()
        }
        onComponentCreated(componentContainer.activityComponent!! as C)
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        ButterKnife.bind(this)
        setupToolbar()
        viewModel = createViewModel()
        onBind()
    }

    override fun onRetainCustomNonConfigurationInstance(): ActivityComponentContainer {
        return componentContainer
    }

    protected abstract fun createComponent(): C

    protected abstract fun onComponentCreated(component: C)

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
        fun <A : RetainComponentActivity<*, *>> start(
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


    class ActivityComponentContainer(
            private val fragmentComponentMap: MutableMap<String, Any>,
            var activityComponent: Any? = null
    ) : ComponentContainer {

        override fun removeByTag(theTag: String) {
            fragmentComponentMap.remove(theTag)
        }

        override fun putWithTag(theTag: String, component: Any) {
            fragmentComponentMap[theTag] = component
        }

        override fun getByTag(theTag: String): Any? = fragmentComponentMap[theTag]
    }
}
