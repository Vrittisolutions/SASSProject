package com.vritti.sass.model;

import java.io.Serializable;

public class Alarm implements Serializable {

    String  TagName,TagValue,IsActive,Addeddt,TagChart;

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    public String getTagValue() {
        return TagValue;
    }

    public void setTagValue(String tagValue) {
        TagValue = tagValue;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getAddeddt() {
        return Addeddt;
    }

    public void setAddeddt(String addeddt) {
        Addeddt = addeddt;
    }

    public String getTagChart() {
        return TagChart;
    }

    public void setTagChart(String tagChart) {
        TagChart = tagChart;
    }
}
