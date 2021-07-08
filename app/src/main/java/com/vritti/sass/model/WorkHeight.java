package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by pradnya on 31-Oct-17.
 */

public class WorkHeight implements Serializable {


    private String Name;
    private boolean selected;

    public WorkHeight() {
        // TODO Auto-generated constructor stub
    }


    public WorkHeight(String name) {
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
