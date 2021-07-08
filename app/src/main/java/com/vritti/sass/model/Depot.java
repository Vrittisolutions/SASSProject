package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 29-Nov-18.
 */

public class Depot implements Serializable {

    String Depotname,Depotid,Depotcode;

    public String getDepotcode() {
        return Depotcode;
    }

    public void setDepotcode(String depotcode) {
        Depotcode = depotcode;
    }

    public String getDepotname() {
        return Depotname;
    }

    public void setDepotname(String depotname) {
        Depotname = depotname;
    }

    public String getDepotid() {
        return Depotid;
    }

    public void setDepotid(String depotid) {
        Depotid = depotid;
    }
}
