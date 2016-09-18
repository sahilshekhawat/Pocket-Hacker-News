package io.github.sahilshekhawat.pockethackernews.Data;

import java.util.ArrayList;
import java.util.Date;

public class Users {
    public Integer id = null;
    public Integer delay = null;
    public Date created = null;
    public Integer karma = null;
    public String about = null;
    public ArrayList<Integer> submitted = null;

    public Users() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setKarma(Integer karma) {
        this.karma = karma;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ArrayList<Integer> getSubmitted() {
        return submitted;
    }

    public void setSubmitted(ArrayList<Integer> submitted) {
        this.submitted = submitted;
    }
}
