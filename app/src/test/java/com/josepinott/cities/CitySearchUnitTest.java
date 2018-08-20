package com.josepinott.cities;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class CitySearchUnitTest {

    private TreeSet<City> mCitiesMap = new TreeSet<>();

    @Test
    public void testLoadCityData() {
        initData();
        // verify data results
        assertEquals("Data size equal to 8", 8, mCitiesMap.size(), 0);
    }

    @Test
    public void testValidSearchCityData() {
        initData();
        List<City> cityList = new ArrayList<>();
        for (City city : mCitiesMap) {
            if (city.mName.toLowerCase().startsWith("Rastnik".toLowerCase()))
                cityList.add(city);
        }
        // verify data results
        assertEquals("Search for cities with prefix: Rastnik", 1, cityList.size(), 0);

        // test for valid input Murava
        cityList.clear();
        for (City city : mCitiesMap) {
            if (city.mName.toLowerCase().startsWith("s".toLowerCase()))
                cityList.add(city);
        }
        // verify data results
        assertEquals("Search for cities with prefix: s", 2, cityList.size(), 0);
    }

    @Test
    public void testInValidSearchCityData() {
        initData();
        List<City> cityList = new ArrayList<>();
        for (City city : mCitiesMap) {
            if (city.mName.toLowerCase().startsWith("!.".toLowerCase()))
                cityList.add(city);
        }
        // verify data results
        assertEquals("Search for cities with prefix: !.", 0, cityList.size(), 0);
    }

    private void initData() {
        try {
            String s = "[" +
                    "{\"country\":\"UA\",\"name\":\"Hurzuf\",\"_id\":707860,\"coord\":{\"lon\":34.283333,\"lat\":44.549999}}," +
                    "{\"country\":\"RU\",\"name\":\"Novinki\",\"_id\":519188,\"coord\":{\"lon\":37.666668,\"lat\":55.683334}}," +
                    "{\"country\":\"NP\",\"name\":\"Gorkhā\",\"_id\":1283378,\"coord\":{\"lon\":84.633331,\"lat\":28}}," +
                    "{\"country\":\"DK\",\"name\":\"Understed\",\"_id\":2610888,\"coord\":{\"lon\":10.51667,\"lat\":57.383331}}," +
                    "{\"country\":\"DK\",\"name\":\"Skodsborg\",\"_id\":2613685,\"coord\":{\"lon\":12.57324,\"lat\":55.822498}}," +
                    "{\"country\":\"DK\",\"name\":\"Smidstrup\",\"_id\":2613357,\"coord\":{\"lon\":12.55787,\"lat\":55.865688}}," +
                    "{\"country\":\"BG\",\"name\":\"Rastnik\",\"_id\":6460975,\"coord\":{\"lon\":25.283331,\"lat\":41.400002}}," +
                    "{\"country\":\"BG\",\"name\":\"Rastnik\",\"_id\":727762,\"coord\":{\"lon\":25.283331,\"lat\":41.400002}}," +
                    "{\"country\":\"LT\",\"name\":\"Murava\",\"_id\":596826,\"coord\":{\"lon\":23.966669,\"lat\":54.916672}}" +
                    "]";
            JSONArray jsonArray = new JSONArray(s);

            Gson gson = new Gson();
            int arraySize = jsonArray.length();

            for(int i=0;i< arraySize; i++) {
                City city = gson.fromJson(jsonArray.getJSONObject(i).toString(), City.class);
                mCitiesMap.add(city);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    class City implements Comparable {

        @SerializedName("country")
        public String mCountry;

        @SerializedName("name")
        public String mName;

        @Override
        public int compareTo(@NonNull Object o) {
            City city = (City) o;
            int result = this.mName.compareTo(city.mName);
            if (result != 0) return result;
            return this.mCountry.compareTo(city.mCountry);
        }
    }
}
