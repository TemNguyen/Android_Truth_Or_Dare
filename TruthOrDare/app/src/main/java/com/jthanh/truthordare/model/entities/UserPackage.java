package com.jthanh.truthordare.model.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserPackage implements Serializable {
    @SerializedName("userId")
    private String userId;
    @SerializedName("packageId")
    private String packageId;

    @Override
    public String toString() {
        return "UserPackage{" +
                "userId='" + userId + '\'' +
                ", packageId='" + packageId + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
