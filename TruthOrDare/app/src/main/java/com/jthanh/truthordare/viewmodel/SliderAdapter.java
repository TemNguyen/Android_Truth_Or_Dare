package com.jthanh.truthordare.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.SliderItemBinding;
import com.jthanh.truthordare.model.Player;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private ArrayList<Player> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAdapter(ArrayList<Player> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderAdapter.SliderViewHolder(
                SliderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.binding.tvPlayer.setText(sliderItems.get(position).getName());
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
        holder.binding.llTruth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("player", sliderItems.get(holder.getAdapterPosition()));
                bundle.putSerializable("type", "truth");
                Navigation.findNavController(holder.itemView).navigate(R.id.gameDetailFragment, bundle);
            }
        });
        holder.binding.llDare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("player", sliderItems.get(holder.getAdapterPosition()));
                bundle.putSerializable("type", "dare");
                Navigation.findNavController(holder.itemView).navigate(R.id.gameDetailFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private final SliderItemBinding binding;

        public SliderViewHolder(final SliderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}
