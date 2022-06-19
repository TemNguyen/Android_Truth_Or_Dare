package com.jthanh.truthordare.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.FragmentHomeBinding;
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

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private static final int RQ_SIGN_IN = 100;
    private static final String TAG = "GG_SIGN_IN_TAG";

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        util = RetrofitUtil.getInstance(getContext());
        appDatabase = AppDatabase.getInstance(getContext());
        questionPackageDao = appDatabase.questionPackageDao();

        loadingDialog = new LoadingDialog(getActivity());
        notificationDialog = new NotificationDialog(getActivity());

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.addPlayerFragment);
            }
        });

        binding.btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionPackageDao.getQuestionPackageCount() == 0) {
                    loadingDialog.startDialog("Đang tải dữ liệu...");
                    util.getAllPackage()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<List<QuestionPackage>>() {
                                @Override
                                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<QuestionPackage> questionPackages) {
                                    loadingDialog.dismissDialog();
                                    if (questionPackages.size() != 0) {
                                        for (QuestionPackage questionPackage:
                                                questionPackages) {
                                            questionPackageDao.insertAllQuestionPackage(questionPackage);
                                        }
                                    } else {
                                        notificationDialog = new NotificationDialog(getActivity());
                                        showMessage("Không có dữ liệu để hiện thị", false);
                                    }
                                    Navigation.findNavController(view).navigate(R.id.addPlayerFragment);
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    loadingDialog.dismissDialog();
                                    notificationDialog = new NotificationDialog(getActivity());
                                    showMessage("Có lỗi xảy ra", false);
                                    e.printStackTrace();
                                }
                            });
                } else {
                    Navigation.findNavController(view).navigate(R.id.addPlayerFragment);
                }

            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Begin sign in");
                try {
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent, RQ_SIGN_IN);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        if (requestCode == RQ_SIGN_IN){
            Log.d(TAG, "Google sign in result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "Firebase auth");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //get user
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        System.out.println(uid + " " + email);

                        // check is new user or existing
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            Log.d(TAG, "New account - add in db");
                        } else {
                            Log.d(TAG, "Exist account - pull question");
                        }

                        Navigation.findNavController(getView()).navigate(R.id.addPlayerFragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Login fail");

                    }
                });
    }

    private void showMessage(String msg, boolean state) {
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}