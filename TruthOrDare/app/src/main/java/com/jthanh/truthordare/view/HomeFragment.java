package com.jthanh.truthordare.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.model.entities.UserPackage;
import com.jthanh.truthordare.model.retrofits.RetrofitUtil;
import com.jthanh.truthordare.model.rooms.AppDatabase;
import com.jthanh.truthordare.model.rooms.QuestionDao;
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
    private FirebaseUser firebaseUser;

    private LoadingDialog loadingDialog;
    private NotificationDialog notificationDialog;

    private RetrofitUtil util;
    private AppDatabase appDatabase;
    private QuestionPackageDao questionPackageDao;
    private QuestionDao questionDao;

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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            logined();
        }

        util = RetrofitUtil.getInstance(getContext());
        appDatabase = AppDatabase.getInstance(getContext());
        questionPackageDao = appDatabase.questionPackageDao();
        questionDao = appDatabase.questionDao();

        loadingDialog = new LoadingDialog(getActivity());
        notificationDialog = new NotificationDialog(getActivity());

        if (questionPackageDao.getAllQuestionPackage().size() == 0) {
            generateDefaultPackage();
        }

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.addPlayerFragment);
            }
        });

        binding.btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.questionFragment);
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

    private void logined() {
        binding.btnLogout.setVisibility(View.VISIBLE);
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                binding.btnLogin.setVisibility(View.VISIBLE);
                binding.tvName.setVisibility(View.INVISIBLE);
                binding.btnLogout.setVisibility(View.INVISIBLE);
            }
        });
        binding.btnLogin.setVisibility(View.INVISIBLE);
        binding.tvName.setVisibility(View.VISIBLE);
        binding.tvName.setText("Xin chào " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName().split(" ")[0]);
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
                        firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        System.out.println(uid + " " + email);

                        // check is new user or existing
                        if (!authResult.getAdditionalUserInfo().isNewUser()) {
                            loadingDialog.startDialog("Đang đăng nhập...");

                            util.getRegisteredQuestionPackage(uid)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableSingleObserver<List<UserPackage>>() {
                                        @Override
                                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<UserPackage> userPackages) {
                                            if (userPackages.size() > 0) {
                                                for (UserPackage item:
                                                     userPackages) {
                                                    if (questionDao.getQuestionByPackageId(item.getPackageId()).size() == 0) {
                                                        util.getAllQuestionByPackageId(item.getPackageId())
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
                                                                    }

                                                                    @Override
                                                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                            loadingDialog.dismissDialog();
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            Log.d(TAG, "Pull question fail");
                                            loadingDialog.dismissDialog();
                                        }
                                    });
                        }
                        logined();
                        Navigation.findNavController(getView()).navigate(R.id.homeFragment);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Login fail");
                    }
                });
    }

    private void generateDefaultPackage() {
        questionPackageDao.insertAllQuestionPackage(new QuestionPackage("0", "Mặc định"));
        questionDao.insertAllQuestion(
                new Question("0", "Hát một bài hát.", "dare", "0"),
                new Question("1", "Vừa chống đẩy vừa hát.", "dare", "0"),
                new Question("2", "Thời gian lâu nhất không tắm?", "truth", "0"),
                new Question("3", "Cover 1 trend tiktok.", "dare", "0"),
                new Question("4", "Cover 1 bài Kpop dance.", "dare", "0"),
                new Question("5", "Đọc ngược bảng chữ cái tiếng Việt.", "dare", "0"),
                new Question("6", "Điều gì làm bạn hối hận nhất?", "truth", "0"),
                new Question("7", "Điều điên rồ nhất đã làm khi say?", "truth", "0"),
                new Question("8", "Điều trẻ con nhất mà bạn từng làm?", "truth", "0"),
                new Question("9", "Điều bạn không bao giờ muốn bố mẹ biết?", "truth", "0"));
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