package com.meetfine.pingyugov.model;

import org.kymjs.kjframe.database.annotate.Id;

/**
 * Created by Andriod05 on 2017/1/6.
 */
public class MyFavor {

    @Id
    private String favorId;
    private String username;
    private String subject;
    private String mill;
    private String avator;
    private int type;//类型 0:ContentDetailActivity  1:GovPicNewsDetailActivity  2:WeBoDetailActivity

    /*ContentDetailActivity*/
    private String ContentId;
    private int ContentType;//类型有：ContentController、OpennessAnnualReportController、opennesscontentController、OpennessGuideController、OpennessRequestController、OpennessRulesController
    private String SiteId;
    private int jump;//跳转的类型



    /*GovPicNewsDetailActivity 及 WeBoDetailActivity*/
    private String id;

    public MyFavor() {
    }

    public String getFavorId() {
        return favorId;
    }

    public void setFavorId(String favorId) {
        this.favorId = favorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMill() {
        return mill;
    }

    public void setMill(String mill) {
        this.mill = mill;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContentId() {
        return ContentId;
    }

    public void setContentId(String contentId) {
        ContentId = contentId;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String siteId) {
        SiteId = siteId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }
    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }
}
