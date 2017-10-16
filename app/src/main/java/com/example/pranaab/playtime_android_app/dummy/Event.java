package com.example.pranaab.playtime_android_app.dummy;

/**
 * Created by pranaab on 2017-10-15.
 */

public class Event {

    private Integer id;
    private String name;
    private String time;
    private Integer num_joined_members;
    private Integer max_members;
    private String location;

    public String getDetails() {
        return details;
    }

    //TODO: Get rid of this, once the final template for detials view is decided.
    public void setDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about: ").append(this.getName());
        builder.append("\n");
        builder.append("Time: ").append(this.getTime());
        builder.append("\n");
        builder.append("Location: ").append(this.getLocation());
        builder.append("\n");
        builder.append("Maximum Members Invited: ").append(this.getMax_members());
        builder.append("\n");
        builder.append("Number Member Joined: ").append(this.getNum_joined_members());
        builder.append("\n");
        this.details = builder.toString();
    }

    private String details;
    public Event(Integer event_id){
        id = event_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNum_joined_members() {
        return num_joined_members;
    }

    public void setNum_joined_members(Integer num_joined_members) {
        this.num_joined_members = num_joined_members;
    }

    public Integer getMax_members() {
        return max_members;
    }

    public void setMax_members(Integer max_members) {
        this.max_members = max_members;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




}
