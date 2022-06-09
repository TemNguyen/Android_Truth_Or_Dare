package com.jthanh.truthordare.model;

import java.io.Serializable;

public class QuestionSelect implements Serializable {
    private int id;
    private String name;

    public QuestionSelect(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
