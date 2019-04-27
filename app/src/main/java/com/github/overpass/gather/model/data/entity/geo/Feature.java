package com.github.overpass.gather.model.data.entity.geo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feature {

    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("place_type")
    private List<String> placeType = null;
    @SerializedName("relevance")
    private int relevance;
    @SerializedName("properties")
    private Properties properties;
    @SerializedName("text")
    private String text;
    @SerializedName("place_name")
    private String placeName;
    @SerializedName("bbox")
    private List<Double> bbox = null;
    @SerializedName("center")
    private List<Integer> center = null;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("context")
    private List<GeoContext> context = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Feature() {
    }

    /**
     * 
     * @param center
     * @param id
     * @param text
     * @param bbox
     * @param context
     * @param relevance
     * @param placeName
     * @param properties
     * @param type
     * @param placeType
     * @param geometry
     */
    public Feature(String id, String type, List<String> placeType, int relevance, Properties properties, String text, String placeName, List<Double> bbox, List<Integer> center, Geometry geometry, List<GeoContext> context) {
        super();
        this.id = id;
        this.type = type;
        this.placeType = placeType;
        this.relevance = relevance;
        this.properties = properties;
        this.text = text;
        this.placeName = placeName;
        this.bbox = bbox;
        this.center = center;
        this.geometry = geometry;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getPlaceType() {
        return placeType;
    }

    public void setPlaceType(List<String> placeType) {
        this.placeType = placeType;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public List<Integer> getCenter() {
        return center;
    }

    public void setCenter(List<Integer> center) {
        this.center = center;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<GeoContext> getContext() {
        return context;
    }

    public void setContext(List<GeoContext> context) {
        this.context = context;
    }

}