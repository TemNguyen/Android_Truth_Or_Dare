package com.jthanh.truthordare.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.CustomActionBarBinding;
import com.jthanh.truthordare.databinding.FragmentLibraryQuestionBinding;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.viewmodel.LibraryQuestionAdapter;

import java.util.ArrayList;

public class LibraryQuestionFragment extends Fragment {
    private FragmentLibraryQuestionBinding binding;
    private CustomActionBarBinding actionBarBinding;

    private ArrayList<QuestionPackage> questionPackages;
    private LibraryQuestionAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        actionBarBinding.tvTitle.setText("Gói câu hỏi có sẵn");
        actionBarBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        questionPackages = new ArrayList<>();
        questionPackages.add(new QuestionPackage("1", "Gói 1"));
        questionPackages.add(new QuestionPackage("2", "Gói 2"));

        adapter = new LibraryQuestionAdapter(questionPackages);
        binding.rvQuestion.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibraryQuestionBinding.inflate(inflater, container, false);
        actionBarBinding = binding.actionBar;
        View view = binding.getRoot();
        return view;
    }
}