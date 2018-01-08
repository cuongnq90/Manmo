package vn.manmo.search.object;

import java.io.Serializable;

/**
 * Created by NguyenQuocCuong on 11/14/2017.
 */

public class NewsObject implements Serializable{
    private String title;
    private String image;
    private String author;
    private String introductory;
    private String time;
    private String id;
    private String url;

    public NewsObject() {
    }

    public NewsObject(String title, String image, String author, String introductory, String time, String id, String url) {
        this.title = title;
        this.image = image;
        this.author = author;
        this.introductory = introductory;
        this.time = time;
        this.id = id;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroductory() {
        return introductory;
    }

    public void setIntroductory(String introductory) {
        this.introductory = introductory;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
