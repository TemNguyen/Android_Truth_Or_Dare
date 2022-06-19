package com.jthanh.truthordare.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.CustomActionBarBinding;
import com.jthanh.truthordare.databinding.FragmentChooseQuestionBinding;
import com.jthanh.truthordare.dialogs.LoadingDialog;
import com.jthanh.truthordare.dialogs.NotificationDialog;
import com.jthanh.truthordare.helper.GameHepler;
import com.jthanh.truthordare.helper.QuestionHelper;
import com.jthanh.truthordare.model.entities.Player;
import com.jthanh.truthordare.model.entities.PackageSelect;
import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.model.retrofits.RetrofitUtil;
import com.jthanh.truthordare.model.rooms.AppDatabase;
import com.jthanh.truthordare.model.rooms.QuestionDao;
import com.jthanh.truthordare.model.rooms.QuestionPackageDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChooseQuestion extends Fragment {
    private FragmentChooseQuestionBinding binding;
    private CustomActionBarBinding actionBarBinding;

    private ArrayList<Player> players;
    private ArrayList<PackageSelect> packageSelects;
    private boolean[] selectedPackage;
    private ArrayList<Integer> selectedPackageId;
    private ArrayList<PackageSelect> packageSelected;

    private RetrofitUtil util;
    private AppDatabase appDatabase;
    private QuestionPackageDao questionPackageDao;
    private QuestionDao questionDao;

    private LoadingDialog loadingDialog;
    private NotificationDialog notificationDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            players = (ArrayList<Player>) getArguments().getSerializable("listPlayer");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        util = RetrofitUtil.getInstance(getContext());
        appDatabase = AppDatabase.getInstance(getContext());
        questionPackageDao = appDatabase.questionPackageDao();
        questionDao = appDatabase.questionDao();

        loadingDialog = new LoadingDialog(getActivity());
        notificationDialog = new NotificationDialog(getActivity());

        actionBarBinding.tvTitle.setText("Chọn bộ câu hỏi");
        actionBarBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        packageSelected = new ArrayList<>();
        packageSelects = new ArrayList<>();
        selectedPackageId = new ArrayList<>();
        int index = 0;
        // handle here
        for (QuestionPackage item:
             questionPackageDao.getAllQuestionPackage()) {
            if (questionDao.getQuestionByPackageId(item.getId()).size() > 0) {
                packageSelects.add(new PackageSelect(index, item));
                index++;
            }
        }
        selectedPackage = new boolean[packageSelects.size()];

        binding.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Chọn gói câu hỏi");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(
                        QuestionHelper.getListQuestion(packageSelects),
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
                            stringBuilder.append(QuestionHelper.getListQuestion(packageSelects)[selectedPackageId.get(j)]);
                            if (j != selectedPackageId.size() - 1) {
                                stringBuilder.append(", ");
                            }
                            packageSelected.add(packageSelects.get(selectedPackageId.get(j)));
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
                if (packageSelected.size() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listPlayer", players);
                    bundle.putSerializable("packageSelected", packageSelected);
                    GameHepler.realItems.clear();
                    GameHepler.realItems.addAll(players);
                    Navigation.findNavController(view).navigate(R.id.gameFragment, bundle);
                } else {
                    notificationDialog = new NotificationDialog(getActivity());
                    showMessage("Vui lòng chọn gói câu hỏi.", "no");
                }

            }
        });
    }

    private void showMessage(String msg, String state) {
        notificationDialog.startDialog(msg, state);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationDialog.dismissDialog();
            }
        }, 1000);
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