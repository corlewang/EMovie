package com.laughing.emovie.entity;

/**
 * Created by Wy on 2018/11/13.
 */

public class MovieDetail {
    private String name;
    private String videoUrl;
    private String videoTxt;
    private String videoImg;
    private String type;
    private String date;//上映时间
    private String duc;//片长
    private String videoContent;//视频获取地址
    private String rating;
    private String comm;
    private String info;

    public String getVideoContent() {
        return videoContent;
    }

    public void setVideoContent(String videoContent) {
        this.videoContent = videoContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoTxt() {
        return videoTxt;
    }

    public void setVideoTxt(String videoTxt) {
        this.videoTxt = videoTxt;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuc() {
        return duc;
    }

    public void setDuc(String duc) {
        this.duc = duc;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
