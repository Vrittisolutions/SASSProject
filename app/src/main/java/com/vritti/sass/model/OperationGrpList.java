package com.vritti.sass.model;

import java.io.Serializable;


public class OperationGrpList implements Serializable {

    String OperationId,OperationDesc;
    boolean isChecked;

    public OperationGrpList(String operationId, String operationDesc) {
        this.OperationId = operationId;
        this.OperationDesc = operationDesc;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getOperationId() {
        return OperationId;
    }

    public void setOperationId(String operationId) {
        OperationId = operationId;
    }

    public String getOperationDesc() {
        return OperationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        OperationDesc = operationDesc;
    }
}
