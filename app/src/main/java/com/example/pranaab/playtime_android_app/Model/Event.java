package com.example.pranaab.playtime_android_app.Model;

import java.util.Date;

/**
 * Created by pranaab on 2017-10-15.
 */

public class Event {

    private String uid;                     // Unique Id for the event
    private String name;                    // Name of the event
    private Integer num_joined_members;     // Members joined already
    private Integer max_subscribers;        // Max subscribers for event
    private String location;                // Location for event
    private String expiry_time;               // Expiry time for the event (time after which it is no longer valid)
    private String start_time;                // Start time for the event
    private String end_time;                  // End time for the event
    private String thumbnail_link;          // Image link

    private String details;

    public Integer getMax_subscribers() {
        return max_subscribers;
    }

    public void setMax_subscribers(Integer max_subscribers) {
        this.max_subscribers = max_subscribers;
    }

    public Event(String uid, String name, Integer num_joined_members, Integer max_subscribers, String location, String expiry_time, String start_time, String end_time, String thumbnail_link) {
        this.uid = uid;
        this.name = name;
        this.num_joined_members = num_joined_members;
        this.max_subscribers = max_subscribers;
        this.location = location;
        this.expiry_time = expiry_time;
        this.start_time = start_time;
        this.end_time = end_time;
        this.thumbnail_link = thumbnail_link;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getThumbnail_link() {
        return thumbnail_link;
    }

    public void setThumbnail_link(String thumbnail_link) {
        this.thumbnail_link = thumbnail_link;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    //TODO: Get rid of this, once the final template for detials view is decided.
    public void setDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about: ").append(this.getName());
        builder.append("\n");
        builder.append("Time: ").append(this.getStart_time());
        builder.append("\n");
        builder.append("Location: ").append(this.getLocation());
        builder.append("\n");
        builder.append("Maximum Members Invited: ").append(this.getMax_subscribers());
        builder.append("\n");
        builder.append("Number Member Joined: ").append(this.getNum_joined_members());
        builder.append("\n");
        this.details = builder.toString();
    }


    public Event(String event_id){
        uid = event_id;
    }

    public String getUId() {
        return uid;
    }

    public void setUId(String id) {
        this.uid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getNum_joined_members() {
        return num_joined_members;
    }

    public void setNum_joined_members(Integer num_joined_members) {
        this.num_joined_members = num_joined_members;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




}
