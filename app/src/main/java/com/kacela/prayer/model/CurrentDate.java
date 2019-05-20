package com.kacela.prayer.model;

import java.util.HashMap;
import java.util.Map;

public class CurrentDate {

    private Integer weekNumber;
    private String utc_offset;
    private String utcDatetime;
    private Integer unixtime;
    private String timezone;
    private Integer rawOffset;
    private Object dstUntil;
    private Integer dstOffset;
    private Object dstFrom;
    private Boolean dst;
    private Integer dayOfYear;
    private Integer dayOfWeek;
    private String datetime;
    private String abbreviation;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getUtcOffset() {
        return utc_offset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utc_offset = utcOffset;
    }

    public String getUtcDatetime() {
        return utcDatetime;
    }

    public void setUtcDatetime(String utcDatetime) {
        this.utcDatetime = utcDatetime;
    }

    public Integer getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(Integer unixtime) {
        this.unixtime = unixtime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getRawOffset() {
        return rawOffset;
    }

    public void setRawOffset(Integer rawOffset) {
        this.rawOffset = rawOffset;
    }

    public Object getDstUntil() {
        return dstUntil;
    }

    public void setDstUntil(Object dstUntil) {
        this.dstUntil = dstUntil;
    }

    public Integer getDstOffset() {
        return dstOffset;
    }

    public void setDstOffset(Integer dstOffset) {
        this.dstOffset = dstOffset;
    }

    public Object getDstFrom() {
        return dstFrom;
    }

    public void setDstFrom(Object dstFrom) {
        this.dstFrom = dstFrom;
    }

    public Boolean getDst() {
        return dst;
    }

    public void setDst(Boolean dst) {
        this.dst = dst;
    }

    public Integer getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(Integer dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "CurrentDate{" +
                "weekNumber=" + weekNumber +
                ", utcOffset='" + utc_offset + '\'' +
                ", utcDatetime='" + utcDatetime + '\'' +
                ", unixtime=" + unixtime +
                ", timezone='" + timezone + '\'' +
                ", rawOffset=" + rawOffset +
                ", dstUntil=" + dstUntil +
                ", dstOffset=" + dstOffset +
                ", dstFrom=" + dstFrom +
                ", dst=" + dst +
                ", dayOfYear=" + dayOfYear +
                ", dayOfWeek=" + dayOfWeek +
                ", datetime='" + datetime + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}