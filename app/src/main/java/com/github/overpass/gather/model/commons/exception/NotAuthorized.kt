package com.github.overpass.gather.model.commons.exception

class NotAuthorized @JvmOverloads constructor(
        message: String = "You are not authorized to access this resource"
) : Exception(message)
