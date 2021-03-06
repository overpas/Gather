package by.overpass.gather.model.entity.geo

import com.google.gson.annotations.SerializedName

class GeoContext(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("short_code")
        var shortCode: String? = null,
        @SerializedName("wikidata")
        var wikidata: String? = null,
        @SerializedName("text")
        var text: String? = null
)