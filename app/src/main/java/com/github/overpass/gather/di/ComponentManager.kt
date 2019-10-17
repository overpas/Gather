package com.github.overpass.gather.di

abstract class ComponentManager<in P, C>(
        private val create: (P) -> C
) {

    protected var component: C? = null

    fun getOrCreate(param: P): C = component ?: create(param)
            .also { component = it }

    fun clear() {
        component = null
    }
}