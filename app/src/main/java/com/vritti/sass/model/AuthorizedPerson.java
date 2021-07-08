package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 29-Nov-18.
 */

public class AuthorizedPerson implements Serializable {

    String Authorizename,Authorizeid;

    String categorydesc,userLoginId;

    public String getCategorydesc() {
        return categorydesc;
    }

    public void setCategorydesc(String categorydesc) {
        this.categorydesc = categorydesc;
    }


    public String getAuthorizename() {
        return Authorizename;
    }

    public void setAuthorizename(String authorizename) {
        Authorizename = authorizename;
    }

    public String getAuthorizeid() {
        return Authorizeid;
    }

    public void setAuthorizeid(String authorizeid) {
        Authorizeid = authorizeid;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }
}
