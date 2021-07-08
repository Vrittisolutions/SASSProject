package com.vritti.sass.model;

import android.content.Context;

import com.vritti.sass.adapter.InstallationListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sharvari on 26-Dec-18.
 */

public class InstallationPreparation implements Serializable{



    String ProjectId,ProjectCode,ProjectName,IsolatedImageName,ElectricalImageName;
    boolean isSelected;
    String eImg,iImg;
    String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsolatedImageName() {
        return IsolatedImageName;
    }

    public void setIsolatedImageName(String isolatedImageName) {
        IsolatedImageName = isolatedImageName;
    }

    public String getElectricalImageName() {
        return ElectricalImageName;
    }

    public void setElectricalImageName(String electricalImageName) {
        ElectricalImageName = electricalImageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getProjectCode() {
        return ProjectCode;
    }

    public void setProjectCode(String projectCode) {
        ProjectCode = projectCode;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String geteImg() {
        return eImg;
    }

    public void seteImg(String eImg) {
        this.eImg = eImg;
    }

    public String getiImg() {
        return iImg;
    }

    public void setiImg(String iImg) {
        this.iImg = iImg;
    }
}
