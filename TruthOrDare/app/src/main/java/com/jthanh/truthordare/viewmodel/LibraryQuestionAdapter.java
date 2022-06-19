package com.jthanh.truthordare.viewmodel;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.jthanh.truthordare.databinding.LibraryQuestionItemBinding;
import com.jthanh.truthordare.dialogs.LoadingDialog;
import com.jthanh.truthordare.dialogs.NotificationDialog;
import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.model.retrofits.RetrofitUtil;
import com.jthanh.truthordare.model.rooms.AppDatabase;
import com.jthanh.truthordare.model.rooms.QuestionDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LibraryQuestionAdapter extends RecyclerView.Adapter<LibraryQuestionAdapter.ViewHolder> {

    private Activity activity;
    private RetrofitUtil util;
    private AppDatabase appDatabase;
    private QuestionDao questionDao;

    private ArrayList<QuestionPackage> questionPackages;

    private LoadingDialog loadingDialog;
    private NotificationDialog notificationDialog;

    public LibraryQuestionAdapter(ArrayList<QuestionPackage> questionPackages, Activity activity) {
        this.questionPackages = questionPackages;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LibraryQuestionAdapter
                .ViewHolder(LibraryQuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        util = RetrofitUtil.getInstance(holder.itemView.getContext());
        appDatabase = AppDatabase.getInstance(holder.itemView.getContext());
        questionDao = appDatabase.questionDao();

        QuestionPackage questionPackage = questionPackages.get(position);

        holder.binding.tvPackageName.setText(questionPackage.getName());
        // Nếu có package rồi thì
        if (questionDao.getQuestionByPackageId(questionPackage.getId()).size() > 0) {
            holder.binding.ivDownloaded.setVisibility(View.VISIBLE);
            holder.binding.btnDownload.setVisibility(View.INVISIBLE);
        } else {
            // Nếu chưa có thì
            holder.binding.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingDialog = new LoadingDialog(activity);
                    loadingDialog.startDialog("Vui lòng chờ...");
                    util.getAllQuestionByPackageId(questionPackage.getId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<List<Question>>() {
                                @Override
                                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Question> questions) {
                                    if (questions.size() > 0) {
                                        for (Question item:
                                             questions) {
                                            questionDao.insertAllQuestion(item);
                                        }
                                    }
                                    loadingDialog.dismissDialog();
                                    notificationDialog = new NotificationDialog(activity);
                                    showMessage("Tải gói câu hỏi thành công", "yes");
                                    registerPackage(questionPackage);
                                    holder.binding.ivDownloaded.setVisibility(View.VISIBLE);
                                    holder.binding.btnDownload.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    loadingDialog.dismissDialog();
                                    notificationDialog = new NotificationDialog(activity);
                                    showMessage("Có lỗi xảy ra.", "no");
                                }
                            });
                }
            });
        }
    }

    public void showMessage(String msg, String state) {
        notificationDialog.startDialog(msg, state);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationDialog.dismissDialog();
            }
        }, 1000);
    }

    private void registerPackage(QuestionPackage questionPackage) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            util.registerQuestionPackage(userId, questionPackage.getId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<String>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull String s) {
                            System.out.println(s);
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
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
