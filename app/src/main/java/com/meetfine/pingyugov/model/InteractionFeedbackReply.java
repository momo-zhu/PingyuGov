package com.meetfine.pingyugov.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by Admin on 2017/8/1.
 */

public class InteractionFeedbackReply {
    private int id;
    private int in_feedback_id;//所属反馈ID
    private int creator_id;//回复人ID
    private String creator_name;//回复人姓名
    private String message;//回复内容
    private Date create_date;//创建日期
    private boolean status;//审核状态
    private Date confirm_date;//审核时间
    private int confirmer_id;//审核人iD
    private String confirmer_name;//审核人姓名
    private boolean removed;//删除状态
    private int site_group_id;//群组ID
    private Date update_date;//更新日期
    private int site_id;//站点

    public InteractionFeedbackReply() {
        this.create_date = new Date();
    }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "in_feedback_id")
    public int getIn_feedback_id() {
        return in_feedback_id;
    }

    @JSONField(name = "in_feedback_id")
    public void setIn_feedback_id(int in_feedback_id) {
        this.in_feedback_id = in_feedback_id;
    }

    @JSONField(name = "creator_id")
    public int getCreator_id() {
        return creator_id;
    }

    @JSONField(name = "creator_id")
    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    @JSONField(name = "creator_name")
    public String getCreator_name() {
        return creator_name;
    }

    @JSONField(name = "creator_name")
    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    @JSONField(name = "message")
    public String getMessage() {
        return message;
    }

    @JSONField(name = "message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JSONField(name = "create_date")
    public Date getCreate_date() {
        return create_date;
    }

    @JSONField(name = "create_date")
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    @JSONField(name = "status")
    public boolean isStatus() {
        return status;
    }

    @JSONField(name = "status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    @JSONField(name = "confirm_date")
    public Date getConfirm_date() {
        return confirm_date;
    }

    @JSONField(name = "confirm_date")
    public void setConfirm_date(Date confirm_date) {
        this.confirm_date = confirm_date;
    }

    @JSONField(name = "confirmer_id")
    public int getConfirmer_id() {
        return confirmer_id;
    }

    @JSONField(name = "confirmer_id")
    public void setConfirmer_id(int confirmer_id) {
        this.confirmer_id = confirmer_id;
    }

    @JSONField(name = "confirmer_name")
    public String getConfirmer_name() {
        return confirmer_name;
    }

    @JSONField(name = "confirmer_name")
    public void setConfirmer_name(String confirmer_name) {
        this.confirmer_name = confirmer_name;
    }

    @JSONField(name = "removed")
    public boolean isRemoved() {
        return removed;
    }

    @JSONField(name = "removed")
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @JSONField(name = "site_group_id")
    public int getSite_group_id() {
        return site_group_id;
    }

    @JSONField(name = "site_group_id")
    public void setSite_group_id(int site_group_id) {
        this.site_group_id = site_group_id;
    }

    @JSONField(name = "update_date")
    public Date getUpdate_date() {
        return update_date;
    }

    @JSONField(name = "update_date")
    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    @JSONField(name = "site_id")
    public int getSite_id() {
        return site_id;
    }

    @JSONField(name = "site_id")
    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }
}
