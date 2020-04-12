package com.bestgo.adsplugin.ads.entity;

public class NewsEntity {
    public final String id;
    public final String title;
    public final String description;
    public final String imgUrl;
    public final String link;
    public final String pubDate;

    public boolean isFacebook = false;
    public boolean isAdmob = false;

    public NewsEntity(String id, String title, String link, String description, String pubDate, String imgUrl) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.imgUrl = imgUrl;
    }
}