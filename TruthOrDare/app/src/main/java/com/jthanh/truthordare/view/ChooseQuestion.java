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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.CustomActionBarBinding;
import com.jthanh.truthordare.databinding.FragmentChooseQuestionBinding;
import com.jthanh.truthordare.model.Player;
import com.jthanh.truthordare.model.QuestionSelect;
import com.jthanh.truthordare.viewmodel.QuestionSelectAdapter;

import java.util.ArrayList;

public class ChooseQuestion extends Fragment {
    private FragmentChooseQuestionBinding binding;
    private CustomActionBarBinding actionBarBinding;

    private ArrayList<Player> players;
    private ArrayList<QuestionSelect> questionSelects;
    private QuestionSelectAdapter adapter;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            players = (ArrayList<Player>) getArguments().getSerializable("listPlayer");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        actionBarBinding.tvTitle.setText("Chọn bộ câu hỏi");
        actionBarBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        questionSelects = new ArrayList<>();
        // handle here
        questionSelects.add(new QuestionSelect(0, "Gói câu hỏi 1"));
        questionSelects.add(new QuestionSelect(1, "Gói câu hỏi 2"));

        adapter = new QuestionSelectAdapter(
                getContext(),
                R.layout.question_select_item,
                questionSelects.toArray(new QuestionSelect[questionSelects.size()]));
        binding.spnQuestion.setAdapter(adapter);
        binding.spnQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long id) {
                position = index;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("listPlayer", players);
                bundle.putSerializable("questionSelect", adapter.getItem(position));
                Navigation.findNavController(view).navigate(R.id.gameFragment, bundle);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChooseQuestionBinding.inflate(inflater, container, false);
        actionBarBinding = binding.actionBar;
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}