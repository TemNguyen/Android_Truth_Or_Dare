package com.jthanh.truthordare.model.retrofits;

import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/package/")
    Single<List<QuestionPackage>> getAllPackage();

    @GET("/package/{packageId}/question/")
    Single<List<Question>> getAllQuestionByPackageId(@Path("packageId") String packageId);
}
