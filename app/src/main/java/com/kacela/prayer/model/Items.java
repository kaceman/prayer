package com.kacela.prayer.model;

public class Items
{
    private String dhuhr;

    private String isha;

    private String shurooq;

    private String asr;

    private String fajr;

    private String date_for;

    private String maghrib;

    public String getDhuhr ()
    {
        return dhuhr;
    }

    public String getIsha ()
    {
        return isha;
    }

    public String getShurooq ()
    {
        return shurooq;
    }

    public String getAsr ()
    {
        return asr;
    }

    public String getFajr ()
    {
        return fajr;
    }

    public String getDate_for ()
    {
        return date_for;
    }

    public String getMaghrib ()
    {
        return maghrib;
    }

    @Override
    public String toString() {
        return "Items{" +
                "dhuhr='" + dhuhr + '\'' +
                ", isha='" + isha + '\'' +
                ", shurooq='" + shurooq + '\'' +
                ", asr='" + asr + '\'' +
                ", fajr='" + fajr + '\'' +
                ", date_for='" + date_for + '\'' +
                ", maghrib='" + maghrib + '\'' +
                '}';
    }
}