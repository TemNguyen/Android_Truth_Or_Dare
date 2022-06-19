package com.jthanh.truthordare.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Question implements Serializable {
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("content")
    private String content;
    @SerializedName("rule")
    private String rule;
    @SerializedName("packageId")
    private String packageId;

    public Question(@NonNull String id, String content, String rule, String packageId) {
        this.id = id;
        this.content = content;
        this.rule = rule;
        this.packageId = packageId;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", rule='" + rule + '\'' +
                ", packageId='" + packageId + '\'' +
                '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
