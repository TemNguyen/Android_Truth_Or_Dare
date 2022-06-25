package com.jthanh.truthordare.view;

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
import com.jthanh.truthordare.databinding.FragmentQuestionBinding;
import com.jthanh.truthordare.dialogs.LoadingDialog;
import com.jthanh.truthordare.dialogs.NotificationDialog;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.model.retrofits.RetrofitUtil;
import com.jthanh.truthordare.model.rooms.AppDatabase;
import com.jthanh.truthordare.model.rooms.QuestionPackageDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuestionFragment extends Fragment {
    private FragmentQuestionBinding binding;
    private CustomActionBarBinding actionBarBinding;

    private RetrofitUtil util;
    private AppDatabase appDatabase;
    private QuestionPackageDao questionPackageDao;

    private LoadingDialog loadingDialog;
    private NotificationDialog notificationDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        util = RetrofitUtil.getInstance(getContext());
        appDatabase = AppDatabase.getInstance(getContext());
        questionPackageDao = appDatabase.questionPackageDao();

        notificationDialog = new NotificationDialog(getActivity());

        actionBarBinding.tvTitle.setText("Câu hỏi");
        actionBarBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        binding.btnLibraryQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startDialog("Vui lòng chờ...");
                util.getAllPackage()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<QuestionPackage>>() {
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<QuestionPackage> questionPackages) {
                                if (questionPackages.size() > 0) {
                                    questionPackageDao.deleteAll();
                                    questionPackageDao.insertAllQuestionPackage(new QuestionPackage("0", "Mặc định"));
                                    for (QuestionPackage item:
                                            questionPackages) {
                                        questionPackageDao.insertAllQuestionPackage(item);
                                    }
                                }
                                loadingDialog.dismissDialog();
                                Navigation.findNavController(view).navigate(R.id.libraryQuestionFragment);
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                loadingDialog.dismissDialog();
                                Navigation.findNavController(view).navigate(R.id.libraryQuestionFragment);
                                e.printStackTrace();
                            }
                        });
            }
        });

        binding.btnCustomQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show dialog here
                showMessage("Tính năng này đang được phát triển", "sad");
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
        binding = FragmentQuestionBinding.inflate(inflater, container, false);
        actionBarBinding = binding.actionBar;
        View view = binding.getRoot();
        return view;
    }
}