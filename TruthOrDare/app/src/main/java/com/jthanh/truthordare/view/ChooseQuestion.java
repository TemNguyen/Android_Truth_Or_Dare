package com.jthanh.truthordare.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.jthanh.truthordare.databinding.FragmentChooseQuestionBinding;
import com.jthanh.truthordare.helper.GameHepler;
import com.jthanh.truthordare.helper.QuestionHelper;
import com.jthanh.truthordare.model.entities.Player;
import com.jthanh.truthordare.model.entities.QuestionSelect;

import java.util.ArrayList;

public class ChooseQuestion extends Fragment {
    private FragmentChooseQuestionBinding binding;
    private CustomActionBarBinding actionBarBinding;

    private ArrayList<Player> players;
    private ArrayList<QuestionSelect> questionSelects;
    private boolean[] selectedPackage;
    private ArrayList<Integer> selectedPackageId = new ArrayList<>();
    private ArrayList<QuestionSelect> packageSelected;

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

        packageSelected = new ArrayList<>();
        questionSelects = new ArrayList<>();
        // handle here
        questionSelects.add(new QuestionSelect(0, "Gói câu hỏi 1"));
        questionSelects.add(new QuestionSelect(1, "Gói câu hỏi 2"));

        selectedPackage = new boolean[questionSelects.size()];

        binding.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Chọn gói câu hỏi");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(
                        QuestionHelper.getListQuestion(questionSelects),
                        selectedPackage,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    selectedPackageId.add(i);
                                } else {
                                    selectedPackageId.remove(i);
                                }
                            }
                        });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < selectedPackageId.size(); j++) {
                            stringBuilder.append(QuestionHelper.getListQuestion(questionSelects)[selectedPackageId.get(j)]);
                            if (j != selectedPackageId.size() - 1) {
                                stringBuilder.append(", ");
                            }
                            packageSelected.add(questionSelects.get(j));
                        }
                        binding.tvSelect.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("listPlayer", players);
                bundle.putSerializable("packageSelected", packageSelected);
                GameHepler.realItems.clear();
                GameHepler.realItems.addAll(players);
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