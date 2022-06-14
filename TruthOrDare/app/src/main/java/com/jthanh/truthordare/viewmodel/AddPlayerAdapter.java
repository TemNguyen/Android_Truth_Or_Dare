package com.jthanh.truthordare.viewmodel;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.AddPlayerItemBinding;
import com.jthanh.truthordare.model.Player;

import java.util.ArrayList;

public class AddPlayerAdapter extends RecyclerView.Adapter<AddPlayerAdapter.ViewHolder> {
    private ArrayList<Player> players;

    public AddPlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public AddPlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AddPlayerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddPlayerAdapter.ViewHolder holder, int position) {
        holder.binding.etPlayer.setText(players.get(position).getName());
        holder.binding.etPlayer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                players.get(holder.getAdapterPosition()).setName(editable.toString());
            }
        });

        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                players.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AddPlayerItemBinding binding;

        public ViewHolder(final AddPlayerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
