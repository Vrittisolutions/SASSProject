package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 26-Dec-18.
 */

public class SafetyMeasure implements Serializable{



        String ProjectId,ProjectCode,ProjectName;
        boolean isSelected;
        String Remarks;
        String imgPath;
        String imgAbsolutePath;

        public String getImgAbsolutePath() {
            return imgAbsolutePath;
        }

        public void setImgAbsolutePath(String imgAbsolutePath) {
            this.imgAbsolutePath = imgAbsolutePath;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
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
        }}
