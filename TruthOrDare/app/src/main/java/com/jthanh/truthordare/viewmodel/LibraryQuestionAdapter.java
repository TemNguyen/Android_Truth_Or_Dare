package com.jthanh.truthordare.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jthanh.truthordare.databinding.LibraryQuestionItemBinding;
import com.jthanh.truthordare.model.entities.QuestionPackage;

import java.util.ArrayList;


public class LibraryQuestionAdapter extends RecyclerView.Adapter<LibraryQuestionAdapter.ViewHolder> {
    private ArrayList<QuestionPackage> questionPackages;

    public LibraryQuestionAdapter(ArrayList<QuestionPackage> questionPackages) {
        this.questionPackages = questionPackages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LibraryQuestionAdapter
                .ViewHolder(LibraryQuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvPackageName.setText(questionPackages.get(position).getName());
        // Nếu có package rồi thì
        holder.binding.ivDownloaded.setVisibility(View.VISIBLE);
        holder.binding.btnDownload.setVisibility(View.INVISIBLE);
        // Nếu chưa có thì
        holder.binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // handle here
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionPackages.isEmpty() ? 0 : questionPackages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LibraryQuestionItemBinding binding;

        public ViewHolder(final LibraryQuestionItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
