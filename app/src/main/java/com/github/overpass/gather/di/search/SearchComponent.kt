package com.github.overpass.gather.di.search

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.search.SearchBottomFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    fun inject(searchBottomFragment: SearchBottomFragment)
}