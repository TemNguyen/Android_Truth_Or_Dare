package com.jthanh.truthordare.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.FragmentGameDetailBinding;
import com.jthanh.truthordare.model.Player;

public class GameDetailFragment extends Fragment {
    private FragmentGameDetailBinding binding;
    private Player player;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            player = (Player) getArguments().getSerializable("player");
            type = (String) getArguments().getSerializable("type");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int backgroundId = type == "truth" ? R.drawable.truth_background : R.drawable.dare_background;
        binding.llMain.setBackgroundResource(backgroundId);
        binding.tvTitle.setText(type);
        binding.tvQuestion.setText("Random question in database");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}