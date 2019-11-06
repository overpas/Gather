package by.overpass.gather.model.entity.geo

import com.google.gson.annotations.SerializedName

class Properties(
        @SerializedName("short_code")
        var shortCode: String? = null,
        @SerializedName("wikidata")
        var wikidata: String? = null
)