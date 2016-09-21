package com.cg.lostfoundapp.model;

public class FoundItem {
    private int id;
    private String name;
    private String dateAndTime;

    public FoundItem() {
    }

    public FoundItem(String name, String dateAndTime) {
        this.name = name;
        this.dateAndTime = dateAndTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
