package com.kacela.prayer.remote;

import com.kacela.prayer.model.HijriData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IHijriService {

    @GET("gToH")
    Call<HijriData> getHijri(@Query("date") String date);

}
