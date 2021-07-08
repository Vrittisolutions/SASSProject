package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by pradnya on 31-Oct-17.
 */

public class SafetyTools implements Serializable {


    private String SafetyToolMasterId,SafetyToolDesc;
    private boolean selected;
    String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    //  boolean ischecked;
/*
    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }*/

    public String getSafetyToolMasterId() {
        return SafetyToolMasterId;
    }

    public SafetyTools() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SafetyTools(String safetyToolDesc, String safetyToolMasterId) {
        SafetyToolDesc = safetyToolDesc;
        SafetyToolMasterId = safetyToolMasterId;

    }

    public void setSafetyToolMasterId(String safetyToolMasterId) {
        SafetyToolMasterId = safetyToolMasterId;
    }

    public String getSafetyToolDesc() {
        return SafetyToolDesc;
    }

    public void setSafetyToolDesc(String safetyToolDesc) {
        SafetyToolDesc = safetyToolDesc;
    }



}
