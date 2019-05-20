package com.kacela.prayer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PrayerData {
    private String name; // Fajr
    private String strTime; // "03:15 (24 h)"
    private int intTime; // 587

    @Override
    public String toString() {
        return "PrayerData{" +
                "name='" + name + '\'' +
                ", strTime='" + strTime + '\'' +
                ", intTime=" + intTime +
                '}';
    }

    public PrayerData(String name, String strTime) {
        this.name = name;
        this.strTime = strTime;

        calculateIntTime();
    }

    private void calculateIntTime() {
        if (strTime != null && !strTime.equals("")) {
            int hours = Integer.parseInt(strTime.split(":")[0]);
            int minutes = Integer.parseInt(strTime.split(":")[1]);

            this.intTime = hours * 60 + minutes;
        }
    }

    public String getName() {
        return name;
    }

    public String getStrTime() {
        return strTime;
    }

    public int getIntTime() {
        return intTime;
    }

    public void setIntTime(int intTime) {
        this.intTime = intTime;
    }
}
