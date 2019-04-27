package com.github.overpass.gather.model.data.entity.geo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoDecode {

    @SerializedName("type")
    private String type;
    @SerializedName("query")
    private List<Double> query = null;
    @SerializedName("features")
    private List<Feature> features = null;
    @SerializedName("attribution")
    private String attribution;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GeoDecode() {
    }

    /**
     * 
     * @param query
     * @param features
     * @param type
     * @param attribution
     */
    public GeoDecode(String type, List<Double> query, List<Feature> features, String attribution) {
        super();
        this.type = type;
        this.query = query;
        this.features = features;
        this.attribution = attribution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getQuery() {
        return query;
    }

    public void setQuery(List<Double> query) {
        this.query = query;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

}