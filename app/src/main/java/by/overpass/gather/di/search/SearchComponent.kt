package by.overpass.gather.di.search

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.search.SearchBottomFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    fun inject(searchBottomFragment: SearchBottomFragment)
}