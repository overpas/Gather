package com.github.overpass.gather.di

abstract class ComponentManager<out C>(private val create: () -> C) {

    private var component: C? = create()

    fun get(): C = component ?: create()

    fun clear() {
        component = null
    }
}