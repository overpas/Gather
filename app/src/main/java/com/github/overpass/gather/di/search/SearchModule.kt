package com.github.overpass.gather.di.search

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}