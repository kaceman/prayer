package com.kacela.prayer.model;

public class Hijri {

    private Month month;

    private String year;

    private String day;

    public Month getMonth ()
    {
        return month;
    }

    public void setMonth (Month month)
    {
        this.month = month;
    }

    public String getYear ()
    {
        return year;
    }

    public void setYear (String year)
    {
        this.year = year;
    }

    public String getDay ()
    {
        return day;
    }

    public void setDay (String day)
    {
        this.day = day;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [month = "+month+", year = "+year+", day = "+day+"]";
    }

}
