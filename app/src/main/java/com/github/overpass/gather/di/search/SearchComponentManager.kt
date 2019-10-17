package com.github.overpass.gather.di.search

import com.github.overpass.gather.di.ComponentManager

class SearchComponentManager(
        searchComponent: SearchComponent
) : ComponentManager<Unit, SearchComponent>({ searchComponent })