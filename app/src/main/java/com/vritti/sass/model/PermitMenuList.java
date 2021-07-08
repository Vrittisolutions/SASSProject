package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 26-Dec-18.
 */

public class PermitMenuList implements Serializable{



    String PKFormId,FormDesc,formcode;


    public String getPKFormId() {
        return PKFormId;
    }

    public void setPKFormId(String PKFormId) {
        this.PKFormId = PKFormId;
    }

    public String getFormDesc() {
        return FormDesc;
    }

    public void setFormDesc(String formDesc) {
        FormDesc = formDesc;
    }

    public String getFormcode() {
        return formcode;
    }

    public void setFormcode(String formcode) {
        this.formcode = formcode;
    }
}
