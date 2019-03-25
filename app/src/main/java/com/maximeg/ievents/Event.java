package com.maximeg.ievents;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Event implements Serializable {
    private String _id;
    private String link;
    private String date;
    private String desc;
    private String img;
    private int position;
    private String state;

    public Event(){}

    public Event(JSONObject jsonObject) throws JSONException {
        this._id = jsonObject.getString("_id");
        this.link = jsonObject.getString("link");
        this.date = jsonObject.getString("date");
        this.desc = jsonObject.getString("desc");
        this.img = jsonObject.getString("img");
        this.position = jsonObject.getInt("position");
        this.state = jsonObject.getString("state");
    }

    public Event(String _id, String link, String date, String desc, String img, int position, String state){
        this._id = _id;
        this.link = link;
        this.date = date;
        this.desc = desc;
        this.img = img;
        this.position = position;
        this.state = state;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString(){
        return "Event{" +
                "_id=" + _id +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                ", desc='" + desc + '\'' +
                ", img='" + img + '\'' +
                ", position='" + position + '\'' +
                ", state'" + state + '\'' +
                '}';
    }
}
