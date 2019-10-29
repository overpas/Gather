package by.overpass.gather.commons.exception

class SubscriptionException @JvmOverloads constructor(
        message: String = "Couldn't onBind to neccessary topic"
) : Throwable(message)
