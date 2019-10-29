package by.overpass.gather.model.data.entity.geo

import com.google.gson.annotations.SerializedName

class Feature(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("place_type")
        var placeType: List<String>? = null,
        @SerializedName("relevance")
        var relevance: Int = 0,
        @SerializedName("properties")
        var properties: Properties? = null,
        @SerializedName("text")
        var text: String? = null,
        @SerializedName("place_name")
        var placeName: String? = null,
        @SerializedName("bbox")
        var bbox: List<Double>? = null,
        @SerializedName("center")
        var center: List<Double>? = null,
        @SerializedName("geometry")
        var geometry: Geometry? = null,
        @SerializedName("context")
        var context: List<GeoContext>? = null
)