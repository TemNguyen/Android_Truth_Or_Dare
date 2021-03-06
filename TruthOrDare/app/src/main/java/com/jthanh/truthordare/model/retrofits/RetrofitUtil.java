package com.jthanh.truthordare.model.retrofits;

import android.content.Context;

import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.model.entities.UserPackage;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

public class RetrofitUtil {
    private static final String BASE_URL = "https://truthordare-game.herokuapp.com/";
    private static RetrofitService service;
    private static RetrofitUtil instance;

    private RetrofitUtil(Context context) {
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
    }

    public static RetrofitUtil getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitUtil(context);
        }

        return instance;
    }

    public Single<List<QuestionPackage>> getAllPackage() {
        return service.getAllPackage();
    }

    public Single<List<Question>> getAllQuestionByPackageId(String packageId) {
        return service.getAllQuestionByPackageId(packageId);
    }

    public Single<String> registerQuestionPackage(String userId, String packageId) {
        return service.registerQuestionPackage(userId, packageId);
    }

    public Single<List<UserPackage>> getRegisteredQuestionPackage(String userId) {
        return service.getRegisteredQuestionPackage(userId);
    }

    public Single<QuestionPackage> getPackageById(String id) {
        return service.getPackageById(id);
    }
}
