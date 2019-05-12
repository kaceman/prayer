package com.kacela.prayer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PrayerData {
    private String name; // Fajr
    private String strTime; // "03:15 am"
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
            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
            try {
                String str24 = date24Format.format(date12Format.parse(strTime));
                int hours = Integer.parseInt(str24.split(":")[0]);
                int minutes = Integer.parseInt(str24.split(":")[1]);

                this.intTime = hours * 60 + minutes;
            } catch (ParseException e) {
                e.printStackTrace();
                this.intTime = 0;
            }
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
