package com.meetfine.pingyugov.model;

/**
 * Created by Andriod05 on 2017/1/16.
 */
public class BranchModel {

    private String branchId;
    private String branchName;

    public BranchModel(String branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
