package com.araboy.natehealthapp;

public class Goal {
    String type, title, desc;

    public Goal(String type, String title, String desc) {
        this.type = type;
        this.title = title;
        this.desc = desc;
    }

    public Goal() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
