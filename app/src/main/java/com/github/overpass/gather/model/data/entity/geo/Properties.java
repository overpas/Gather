package com.github.overpass.gather.model.data.entity.geo;

import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("short_code")
    private String shortCode;
    @SerializedName("wikidata")
    private String wikidata;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Properties() {
    }

    /**
     * 
     * @param wikidata
     * @param shortCode
     */
    public Properties(String shortCode, String wikidata) {
        super();
        this.shortCode = shortCode;
        this.wikidata = wikidata;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getWikidata() {
        return wikidata;
    }

    public void setWikidata(String wikidata) {
        this.wikidata = wikidata;
    }

}