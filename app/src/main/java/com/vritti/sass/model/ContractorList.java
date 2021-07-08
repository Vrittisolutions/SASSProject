package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 29-Nov-18.
 */

public class ContractorList implements Serializable {

    String CustVendorMasterId;
    String CustVendorName;
    String CustVendorCode;
    String PermitNo;

    public String getPermitNo() {
        return PermitNo;
    }

    public void setPermitNo(String permitNo) {
        PermitNo = permitNo;
    }

    public String getCustVendorCode() {
        return CustVendorCode;
    }

    public void setCustVendorCode(String custVendorCode) {
        CustVendorCode = custVendorCode;
    }



    public String getCustVendorMasterId() {
        return CustVendorMasterId;
    }

    public void setCustVendorMasterId(String custVendorMasterId) {
        CustVendorMasterId = custVendorMasterId;
    }

    public String getCustVendorName() {
        return CustVendorName;
    }

    public void setCustVendorName(String custVendorName) {
        CustVendorName = custVendorName;
    }
}
