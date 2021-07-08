package com.vritti.sass.model;

import java.io.Serializable;

public class Question implements Serializable {

    String shifthandoverMasterId,ShifthandoverFrom,ShifthandoverTo,PlantDowngardeCondition,Hotwork,LotoPermit,
    PermitIssue,PlantStatus,FromTime,ToTime,Remark,Que1,Que2,Que3,Ans1,Ans2,Ans3,AddedBy;

    String qId ="", question_ans="";

    String  ShifthandoverQuestionMasterId,Question,Tagname,Error;

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getQuestion_ans() {
        return question_ans;
    }

    public void setQuestion_ans(String question_ans) {
        this.question_ans = question_ans;
    }

    public String getShifthandoverQuestionMasterId() {
        return ShifthandoverQuestionMasterId;
    }

    public void setShifthandoverQuestionMasterId(String shifthandoverQuestionMasterId) {
        ShifthandoverQuestionMasterId = shifthandoverQuestionMasterId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getTagname() {
        return Tagname;
    }

    public void setTagname(String tagname) {
        Tagname = tagname;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getShifthandoverMasterId() {
        return shifthandoverMasterId;
    }

    public void setShifthandoverMasterId(String shifthandoverMasterId) {
        this.shifthandoverMasterId = shifthandoverMasterId;
    }

    public String getShifthandoverFrom() {
        return ShifthandoverFrom;
    }

    public void setShifthandoverFrom(String shifthandoverFrom) {
        ShifthandoverFrom = shifthandoverFrom;
    }

    public String getShifthandoverTo() {
        return ShifthandoverTo;
    }

    public void setShifthandoverTo(String shifthandoverTo) {
        ShifthandoverTo = shifthandoverTo;
    }

    public String getPlantDowngardeCondition() {
        return PlantDowngardeCondition;
    }

    public void setPlantDowngardeCondition(String plantDowngardeCondition) {
        PlantDowngardeCondition = plantDowngardeCondition;
    }

    public String getHotwork() {
        return Hotwork;
    }

    public void setHotwork(String hotwork) {
        Hotwork = hotwork;
    }

    public String getLotoPermit() {
        return LotoPermit;
    }

    public void setLotoPermit(String lotoPermit) {
        LotoPermit = lotoPermit;
    }

    public String getPermitIssue() {
        return PermitIssue;
    }

    public void setPermitIssue(String permitIssue) {
        PermitIssue = permitIssue;
    }

    public String getPlantStatus() {
        return PlantStatus;
    }

    public void setPlantStatus(String plantStatus) {
        PlantStatus = plantStatus;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getQue1() {
        return Que1;
    }

    public void setQue1(String que1) {
        Que1 = que1;
    }

    public String getQue2() {
        return Que2;
    }

    public void setQue2(String que2) {
        Que2 = que2;
    }

    public String getQue3() {
        return Que3;
    }

    public void setQue3(String que3) {
        Que3 = que3;
    }

    public String getAns1() {
        return Ans1;
    }

    public void setAns1(String ans1) {
        Ans1 = ans1;
    }

    public String getAns2() {
        return Ans2;
    }

    public void setAns2(String ans2) {
        Ans2 = ans2;
    }

    public String getAns3() {
        return Ans3;
    }

    public void setAns3(String ans3) {
        Ans3 = ans3;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }
}
