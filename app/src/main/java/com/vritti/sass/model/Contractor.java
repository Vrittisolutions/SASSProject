package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 30-Nov-18.
 */

public class Contractor  {

    String ContractorName,ContractorMobile,CustVendorCode,CustVendorMasterId;
    String InstrumentDescription,MaintenanceStartTime,MaintenanceActiveTime;

    public String getInstrumentDescription() {
        return InstrumentDescription;
    }

    public void setInstrumentDescription(String instrumentDescription) {
        InstrumentDescription = instrumentDescription;
    }

    public String getMaintenanceStartTime() {
        return MaintenanceStartTime;
    }

    public void setMaintenanceStartTime(String maintenanceStartTime) {
        MaintenanceStartTime = maintenanceStartTime;
    }

    public String getMaintenanceActiveTime() {
        return MaintenanceActiveTime;
    }

    public void setMaintenanceActiveTime(String maintenanceActiveTime) {
        MaintenanceActiveTime = maintenanceActiveTime;
    }

    public Contractor() {
    }

    public Contractor(String contractorName, String contractorMobile) {
        ContractorName = contractorName;
        ContractorMobile = contractorMobile;
    }

    public String getCustVendorMasterId() {
        return CustVendorMasterId;
    }

    public void setCustVendorMasterId(String custVendorMasterId) {
        CustVendorMasterId = custVendorMasterId;
    }

    public String getCustVendorCode() {
        return CustVendorCode;
    }

    public void setCustVendorCode(String custVendorCode) {
        CustVendorCode = custVendorCode;
    }

    public String getContractorName() {
        return ContractorName;
    }

    public void setContractorName(String contractorName) {
        ContractorName = contractorName;
    }

    public String getContractorMobile() {
        return ContractorMobile;
    }

    public void setContractorMobile(String contractorMobile) {
        ContractorMobile = contractorMobile;
    }
}
