package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by pradnya on 31-Oct-17.
 */

public class Cleansing implements Serializable {


    private String Name,Id;
    private boolean selected;

    public Cleansing() {
    }

    public Cleansing(String name, String id) {
        Name = name;
        Id = id;
    }

    public Cleansing(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
