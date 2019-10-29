package by.overpass.gather.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
        private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelsMap[modelClass]
            ?.get()
            ?.let { it as T }
            ?: viewModelsMap.asIterable()
                    .firstOrNull { modelClass.isAssignableFrom(it.key) }
                    ?.value
                    ?.get()
                    ?.let { it as T }
            ?: throw IllegalArgumentException("unknown model class $modelClass")

}