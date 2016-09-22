package io.github.sahilshekhawat.pockethackernews.Data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sahil on 9/18/16.
 */
public class Items {
    public Long id = null;
    public Boolean deleted = null;
    public ItemType type = null;
    public String by = null; //author's username
    public Long time = null;
    public String text = null;
    public Boolean dead = null;
    public Long parent = null;
    public ArrayList<Long> kids = null;
    public String url = null;
    public Long score = null;
    public String title = null;
    public ArrayList<Long> parts = null;
    public Long descendants = null;

    //Snacktory data
    public String articleTitle = null;
    public String articleText = null;
    public String articleImageURL = null;

    public Items(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(String type) {
        if(type.equals("story"))
            this.type = ItemType.STORY;
        if(type.equals("comment"))
            this.type = ItemType.COMMENT;
        if(type.equals("job"))
            this.type = ItemType.JOB;
        if(type.equals("poll"))
            this.type = ItemType.POLL;
        if(type.equals("pollopt"))
            this.type = ItemType.POLLOPT;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getArticleImageURL() {
        return articleImageURL;
    }

    public void setArticleImageURL(String articleImageURL) {
        this.articleImageURL = articleImageURL;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getDescendants() {
        return descendants;
    }

    public void setDescendants(Long descendants) {
        this.descendants = descendants;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Long> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Long> parts) {
        this.parts = parts;
    }

    public ArrayList<Long> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Long> kids) {
        this.kids = kids;
    }

}
