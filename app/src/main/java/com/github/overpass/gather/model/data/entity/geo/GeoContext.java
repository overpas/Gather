package com.github.overpass.gather.model.data.entity.geo;

import com.google.gson.annotations.SerializedName;

public class GeoContext {

    @SerializedName("id")
    private String id;
    @SerializedName("short_code")
    private String shortCode;
    @SerializedName("wikidata")
    private String wikidata;
    @SerializedName("text")
    private String text;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GeoContext() {
    }

    /**
     * 
     * @param id
     * @param text
     * @param wikidata
     * @param shortCode
     */
    public GeoContext(String id, String shortCode, String wikidata, String text) {
        super();
        this.id = id;
        this.shortCode = shortCode;
        this.wikidata = wikidata;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}