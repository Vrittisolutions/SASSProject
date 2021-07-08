package com.vritti.sass;

import java.io.Serializable;

/**
 * Created by sharvari on 15-Nov-18.
 */

public class DowngradeCondition implements Serializable {

    String Message;
    String DowngradConditions,DateTime1;

    public String getDowngradConditions() {
        return DowngradConditions;
    }

    public void setDowngradConditions(String downgradConditions) {
        DowngradConditions = downgradConditions;
    }

    public String getDateTime1() {
        return DateTime1;
    }

    public void setDateTime1(String dateTime1) {
        DateTime1 = dateTime1;
    }

    public String getMessage() {
        return Message;
    }

    public DowngradeCondition(String message) {
        Message = message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public DowngradeCondition() {
    }
}
