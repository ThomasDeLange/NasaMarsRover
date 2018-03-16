package com.example.thomas.nasamarsroverphotoapp.domain;

import java.io.Serializable;

/**
 * Created by thomas on 13-03-18.
 */

public class RoverPhoto implements Serializable {

    private String imgUrl;
    private String imgId;
    private String fullName;

    public RoverPhoto(String imgUrl, String imgId, String fullName) {
        this.imgUrl = imgUrl;
        this.imgId = imgId;
        this.fullName = fullName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "RoverPhoto{" +
                ", imgId='" + imgId + '\'' +
                ", fullName='" + fullName + '\'' +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
