package by.overpass.gather.commons.exception

class PhotoUploadException @JvmOverloads constructor(
        message: String = "Couldn't upload photo"
) : Exception(message)
