package com.kacela.prayer.model;

public class Data {

    private Hijri hijri;

    public Hijri getHijri ()
    {
        return hijri;
    }

    public void setHijri (Hijri hijri)
    {
        this.hijri = hijri;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [hijri = "+hijri+"]";
    }

}
