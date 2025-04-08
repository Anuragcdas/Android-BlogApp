package com.example.exe1;

public class Photo {
    private String title;
    private String url;
    private String thumbnail;

    public Photo(String title, String url, String thumbnail) {
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}
