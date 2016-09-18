package io.github.sahilshekhawat.pockethackernews.Data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sahil on 9/18/16.
 */
public class Items {
    public Integer id = null;
    public Boolean deleted = null;
    public ItemType type = null;
    public String by = null; //author's username
    public Date time = null;
    public String text = null;
    public Boolean dead = null;
    public Integer parent = null;
    public ArrayList<Integer> kids = null;
    public String url = null;
    public Integer score = null;
    public String title = null;
    public ArrayList<Integer> parts = null;
    public Integer descendants = null;

    public Items(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public void setType(ItemType type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public ArrayList<Integer> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Integer> kids) {
        this.kids = kids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Integer> parts) {
        this.parts = parts;
    }

    public Integer getDescendants() {
        return descendants;
    }

    public void setDescendants(Integer descendants) {
        this.descendants = descendants;
    }
}
