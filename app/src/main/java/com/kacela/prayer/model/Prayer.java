package com.kacela.prayer.model;

import java.util.Arrays;

public class Prayer
{
    private String country;

    private String city;

    private String timezone;

    private Items[] items;

    private Today_weather today_weather;

    public String getCountry ()
    {
        return country;
    }

    public String getCity ()
    {
        return city;
    }

    public Items[] getItems ()
    {
        return items;
    }

    public Today_weather getToday_weather ()
    {
        return today_weather;
    }

    @Override
    public String toString() {
        return "Prayer{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", timezone='" + timezone + '\'' +
                ", items=" + Arrays.toString(items) +
                ", today_weather=" + today_weather +
                '}';
    }
}
