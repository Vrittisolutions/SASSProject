package com.vritti.sass.model;

import java.io.Serializable;

public class ItemDetail implements Serializable {

    private long id;
    private int imgId;
    private String name;
    private String descr;



    public ItemDetail(String name, String descr) {
        this.id = id;
        this.imgId = imgId;
        this.name = name;
        this.descr = descr;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescr() {
        return descr;
    }
    public void setDescr(String descr) {
        this.descr = descr;
    }


}