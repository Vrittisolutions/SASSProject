package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 21-Dec-18.
 */

public class EquipmentUse implements Serializable {



    String PKQuesID,QuesCode,QuesText,SelectionText,SelectionValue;


    private boolean selected;
    String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPKQuesID() {
        return PKQuesID;
    }

    public void setPKQuesID(String PKQuesID) {
        this.PKQuesID = PKQuesID;
    }

    public String getQuesCode() {
        return QuesCode;
    }

    public void setQuesCode(String quesCode) {
        QuesCode = quesCode;
    }

    public String getQuesText() {
        return QuesText;
    }

    public void setQuesText(String quesText) {
        QuesText = quesText;
    }

    public String getSelectionText() {
        return SelectionText;
    }

    public void setSelectionText(String selectionText) {
        SelectionText = selectionText;
    }

    public String getSelectionValue() {
        return SelectionValue;
    }

    public void setSelectionValue(String selectionValue) {
        SelectionValue = selectionValue;
    }
}
