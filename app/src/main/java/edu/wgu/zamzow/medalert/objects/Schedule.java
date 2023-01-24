package edu.wgu.zamzow.medalert.objects;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String name;
    private String time;

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
