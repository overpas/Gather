package com.github.overpass.gather.screen.base.retain

interface ComponentContainer {

    fun removeByTag(theTag: String)

    fun putWithTag(theTag: String, component: Any)

    fun getByTag(theTag: String): Any?
}