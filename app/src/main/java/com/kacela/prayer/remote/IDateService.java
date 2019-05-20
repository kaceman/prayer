package com.kacela.prayer.remote;

import com.kacela.prayer.model.CurrentDate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDateService {

    @GET("{tzId}")
    Call<CurrentDate> getDate(@Path("tzId") String tzId);

}
