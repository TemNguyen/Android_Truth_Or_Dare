package com.jthanh.truthordare.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.jthanh.truthordare.databinding.FragmentGameBinding;
import com.jthanh.truthordare.helper.GameHepler;
import com.jthanh.truthordare.model.entities.Player;
import com.jthanh.truthordare.model.entities.PackageSelect;
import com.jthanh.truthordare.viewmodel.SliderAdapter;

import java.util.ArrayList;
import java.util.Random;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;
    private Handler sliderHandler = new Handler();

    private ArrayList<Player> players;
    private ArrayList<PackageSelect> packageSelected;

    private Random random;

    private int times = 3; // Số vòng quay qua vị trí cần chọn
    private int selected = 0; // Số thứ tự của item cần chọn
    private int length = 0; // Độ dài ban đầu của mảng item
    private int lastNumber = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            players = (ArrayList<Player>) getArguments().getSerializable("listPlayer");
            packageSelected = (ArrayList<PackageSelect>) getArguments().getSerializable("packageSelected");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.vpSlider.setVisibility(View.INVISIBLE);
        binding.clMain.setVisibility(View.VISIBLE);
        if (GameHepler.flag == true) {
            players.clear();
            players.addAll(GameHepler.realItems);
            GameHepler.flag = false;
        }

        length = players.size();
        binding.vpSlider.setAdapter(new SliderAdapter(players, binding.vpSlider, packageSelected, getActivity()));
        binding.vpSlider.setClipToPadding(false);
        binding.vpSlider.setClipChildren(false);
        binding.vpSlider.setOffscreenPageLimit(3);
        binding.vpSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.vpSlider.setVisibility(View.VISIBLE);
                binding.clMain.setVisibility(View.INVISIBLE);
                // handle random selected player
                random = new Random();
                do {
                    selected = random.nextInt(length);
                } while (selected == lastNumber);
                lastNumber = selected;
                times = random.nextInt(5 - 2) + 2;
                // handle spin
                binding.clMain.setVisibility(View.GONE);
                sliderHandler.postDelayed(sliderRunnable, 100);
                binding.vpSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        sliderHandler.removeCallbacks(sliderRunnable);
                        if (position == selected + (times - 1) * length) {
                            GameHepler.flag = true;
                            return;
                        }
                        sliderHandler.postDelayed(sliderRunnable, 100);
                    }
                });
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.vpSlider.setCurrentItem(binding.vpSlider.getCurrentItem() + 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}