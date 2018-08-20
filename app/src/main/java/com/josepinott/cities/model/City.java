package com.josepinott.cities.model;

import com.google.gson.annotations.SerializedName;

/**
 * Class used for a city location.
 */
public class City {

    @SerializedName("country")
    public String mCountry;

    @SerializedName("name")
    public String mName;

    @SerializedName("_id")
    public int mId;

    @SerializedName("coord")
    public Coordinates mCoordinates;

    public String mDisplayLabel;

    /**
     * Coordinates class allows Gson to serialize the coordinates.
     */
    public class Coordinates {
        @SerializedName("lon")
        double mLatitude;
        @SerializedName("lat")
        double mLongitude;
    }
}
