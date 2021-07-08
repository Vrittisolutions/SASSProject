package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 29-Nov-18.
 */

public class Location implements Serializable {

    String LocationMasterId,LocationDesc;

    public String getLocationMasterId() {
        return LocationMasterId;
    }

    public void setLocationMasterId(String locationMasterId) {
        LocationMasterId = locationMasterId;
    }

    public String getLocationDesc() {
        return LocationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        LocationDesc = locationDesc;
    }
}
