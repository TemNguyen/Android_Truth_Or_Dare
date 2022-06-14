package com.jthanh.truthordare.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.CustomActionBarBinding;
import com.jthanh.truthordare.databinding.FragmentGameBinding;
import com.jthanh.truthordare.model.Player;
import com.jthanh.truthordare.model.QuestionSelect;

import java.util.ArrayList;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;
    private CustomActionBarBinding actionBarBinding;

    private ArrayList<Player> players;
    private ArrayList<QuestionSelect> packageSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            players = (ArrayList<Player>) getArguments().getSerializable("listPlayer");
            packageSelected = (ArrayList<QuestionSelect>) getArguments().getSerializable("packageSelected");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        for (Player player : players) {
            Log.d("DEBUG", player.getName());
        }

        for (QuestionSelect question : packageSelected) {
            Log.d("DEBUG", question.getName());
        }
        // Handle here


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
//        actionBarBinding = binding.actionBar;
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}