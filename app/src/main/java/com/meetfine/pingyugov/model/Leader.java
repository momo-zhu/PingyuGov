package com.meetfine.pingyugov.model;

/**
 * Created by Andriod05 on 2017/1/13.
 */
public class Leader {
    private int id;
    private String name;
    private String job_title;
    private String duty;
    private boolean showDuty;

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

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public boolean isShowDuty() {
        return showDuty;
    }

    public void setShowDuty(boolean showDuty) {
        this.showDuty = showDuty;
    }
}
