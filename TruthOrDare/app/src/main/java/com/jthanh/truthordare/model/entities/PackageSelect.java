package com.jthanh.truthordare.model.entities;

import java.io.Serializable;

public class PackageSelect implements Serializable {
    private int id;
    private QuestionPackage questionPackage;
    private boolean selected;

    public PackageSelect(int id, QuestionPackage questionPackage) {
        this.id = id;
        this.questionPackage = questionPackage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuestionPackage getQuestionPackage() {
        return questionPackage;
    }

    public void setQuestionPackage(QuestionPackage questionPackage) {
        this.questionPackage = questionPackage;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
