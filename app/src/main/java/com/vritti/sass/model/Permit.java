package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 30-Nov-18.
 */

public class Permit implements Serializable {

    String PermitId,PermitNo,Nature_Operation,Location_operation,AddedDt,PermitName,
            WorkAuthorizationMasterId,WorkAuthorizationstatus;

    String IsolatedImageName,ElectricalImageName,Attachment,Attachment1,AddedDt1,SpotImage;


    public String getSpotImage() {
        return SpotImage;
    }

    public void setSpotImage(String spotImage) {
        SpotImage = spotImage;
    }

    public String getIsolatedImageName() {
        return IsolatedImageName;
    }

    public void setIsolatedImageName(String isolatedImageName) {
        IsolatedImageName = isolatedImageName;
    }

    public String getElectricalImageName() {
        return ElectricalImageName;
    }

    public void setElectricalImageName(String electricalImageName) {
        ElectricalImageName = electricalImageName;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public String getAttachment1() {
        return Attachment1;
    }

    public void setAttachment1(String attachment1) {
        Attachment1 = attachment1;
    }

    public String getAddedDt1() {
        return AddedDt1;
    }

    public void setAddedDt1(String addedDt1) {
        AddedDt1 = addedDt1;
    }

    public Permit(String permitNo, String nature_Operation, String location_operation, String AddedDt) {
        PermitNo = permitNo;
        Nature_Operation = nature_Operation;
        Location_operation = location_operation;
        AddedDt = AddedDt;
    }

    public String getWorkAuthorizationstatus() {
        return WorkAuthorizationstatus;
    }

    public void setWorkAuthorizationstatus(String workAuthorizationstatus) {
        WorkAuthorizationstatus = workAuthorizationstatus;
    }

    public String getWorkAuthorizationMasterId() {
        return WorkAuthorizationMasterId;
    }

    public void setWorkAuthorizationMasterId(String workAuthorizationMasterId) {
        WorkAuthorizationMasterId = workAuthorizationMasterId;
    }

    public Permit() {
    }

    public Permit(String permitName) {
        PermitName = permitName;
    }

    public String getPermitName() {
        return PermitName;
    }

    public void setPermitName(String permitName) {
        PermitName = permitName;
    }
    public String getPermitId() {
        return PermitId;
    }

    public void setPermitId(String permitId) {
        PermitId = permitId;
    }

    public String getPermitNo() {
        return PermitNo;
    }

    public void setPermitNo(String permitNo) {
        PermitNo = permitNo;
    }

    public String getNature_Operation() {
        return Nature_Operation;
    }

    public void setNature_Operation(String nature_Operation) {
        Nature_Operation = nature_Operation;
    }

    public String getLocation_operation() {
        return Location_operation;
    }

    public void setLocation_operation(String location_operation) {
        Location_operation = location_operation;
    }

    public String getAddedDt() {
        return AddedDt;
    }

    public void setAddedDt(String addedDt) {
        AddedDt = addedDt;
    }
}
