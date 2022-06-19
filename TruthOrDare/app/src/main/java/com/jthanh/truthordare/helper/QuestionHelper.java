package com.jthanh.truthordare.helper;

import com.jthanh.truthordare.model.entities.PackageSelect;

import java.util.ArrayList;

public class QuestionHelper {
    public static ArrayList<Integer> getListID(ArrayList<PackageSelect> packageSelects) {
        ArrayList<Integer> result = new ArrayList<>();
        for (PackageSelect question : packageSelects) {
            result.add(question.getId());
        }
        return result;
    }

    public static String[] getListQuestion(ArrayList<PackageSelect> packageSelects) {
        ArrayList<String> result = new ArrayList<>();
        for (PackageSelect packageSelect : packageSelects) {
            result.add(packageSelect.getQuestionPackage().getName());
        }
        return result.toArray(new String[result.size()]);
    }
}
