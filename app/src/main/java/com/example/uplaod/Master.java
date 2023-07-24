package com.example.uplaod;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @PROJECT_NAME: finalTest
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/7/24 9:06
 */
public class Master {

    private String positivePoints;

    @JsonProperty("side_points")
    private String sidePoints;

    @JsonProperty("positive_img")
    private String positiveImg;

    @JsonProperty("side_img")
    private String sideImg;

    @JsonProperty("location_positive_height")
    private double locationPositiveHeight;

    @JsonProperty("location_side_height")
    private double locationSideHeight;

    @JsonProperty("positive_points")
    public String getPositivePoints() {
        return positivePoints;
    }

    public void setPositivePoints(String positivePoints) {
        this.positivePoints = positivePoints;
    }

    public String getSidePoints() {
        return sidePoints;
    }

    public void setSidePoints(String sidePoints) {
        this.sidePoints = sidePoints;
    }

    public String getPositiveImg() {
        return positiveImg;
    }

    public void setPositiveImg(String positiveImg) {
        this.positiveImg = positiveImg;
    }

    public String getSideImg() {
        return sideImg;
    }

    public void setSideImg(String sideImg) {
        this.sideImg = sideImg;
    }

    public double getLocationPositiveHeight() {
        return locationPositiveHeight;
    }

    public void setLocationPositiveHeight(double locationPositiveHeight) {
        this.locationPositiveHeight = locationPositiveHeight;
    }

    public double getLocationSideHeight() {
        return locationSideHeight;
    }

    public void setLocationSideHeight(double locationSideHeight) {
        this.locationSideHeight = locationSideHeight;
    }
}
