package com.github.overpass.gather.model.commons.exception

class SubscriptionException @JvmOverloads constructor(
        message: String = "Couldn't onBind to neccessary topic"
) : Throwable(message)
