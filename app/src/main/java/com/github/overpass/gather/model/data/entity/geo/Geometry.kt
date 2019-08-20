package com.github.overpass.gather.model.data.entity.geo

import com.google.gson.annotations.SerializedName

class Geometry(
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("coordinates")
        var coordinates: List<Double>? = null
)