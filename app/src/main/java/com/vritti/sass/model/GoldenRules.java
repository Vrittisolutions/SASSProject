package com.vritti.sass.model;

public class GoldenRules {


    String GoldenRulesId,Seq,GoldenRulesDesc;
    boolean isSelected;


    public String getGoldenRulesId() {
        return GoldenRulesId;
    }

    public void setGoldenRulesId(String goldenRulesId) {
        GoldenRulesId = goldenRulesId;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String seq) {
        Seq = seq;
    }

    public String getGoldenRulesDesc() {
        return GoldenRulesDesc;
    }

    public void setGoldenRulesDesc(String goldenRulesDesc) {
        GoldenRulesDesc = goldenRulesDesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return this.GoldenRulesDesc;
    }
}
