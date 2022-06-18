package com.jthanh.truthordare.helper;

import com.jthanh.truthordare.model.entities.QuestionSelect;

import java.util.ArrayList;

public class QuestionHelper {
    public static ArrayList<Integer> getListID(ArrayList<QuestionSelect> questionSelects) {
        ArrayList<Integer> result = new ArrayList<>();
        for (QuestionSelect question : questionSelects) {
            result.add(question.getId());
        }
        return result;
    }

    public static String[] getListQuestion(ArrayList<QuestionSelect> questionSelects) {
        ArrayList<String> result = new ArrayList<>();
        for (QuestionSelect question : questionSelects) {
            result.add(question.getName());
        }
        return result.toArray(new String[result.size()]);
    }
}
