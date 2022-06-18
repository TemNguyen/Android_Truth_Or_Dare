package com.jthanh.truthordare.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity
public class QuestionPackage implements Serializable {
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("name")
    private String name;

    @Override
    public String toString() {
        return "QuestionPackage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
