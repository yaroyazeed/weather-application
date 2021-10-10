package com.yaroyazeed.weatherinfo;

public class CityItem {
    private int mCityImage;
    private String mCityName;
    private String mCityCountry;

    public CityItem(int cityImage, String cityName, String cityCountry){
        mCityImage = cityImage;
        mCityName = cityName;
        mCityCountry = cityCountry;
    }

    public String getCityCountry() {
        return mCityCountry;
    }

    public int getCityImage() {
        return mCityImage;
    }

    public String getCityName() {
        return mCityName;
    }
}
