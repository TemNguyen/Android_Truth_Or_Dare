package com.jthanh.truthordare.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.model.Player;

import java.util.ArrayList;

public class ChooseQuestion extends Fragment {
    private ArrayList<Player> players;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            players = (ArrayList<Player>) getArguments().getSerializable("listPlayer");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        for (Player player : players) {
            Log.d("DEBUG", player.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_choose_question, container, false);
    }
}