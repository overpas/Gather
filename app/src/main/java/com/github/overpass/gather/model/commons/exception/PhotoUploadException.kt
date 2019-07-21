package com.github.overpass.gather.model.commons.exception

class PhotoUploadException @JvmOverloads constructor(
        message: String = "Couldn't upload photo"
) : Exception(message)
