package by.overpass.gather.model.entity.geo

import com.google.gson.annotations.SerializedName

class GeoDecode(
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("query")
        var query: List<Double>? = null,
        @SerializedName("features")
        var features: List<Feature>? = null,
        @SerializedName("attribution")
        var attribution: String? = null
)