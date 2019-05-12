package com.kacela.prayer.remote;

import com.kacela.prayer.model.Prayer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IPrayerService {

    @GET("{city}/daily.json?key=67e38116f7bb3529a703902670c46524")
    Call<Prayer> getPrayer(@Path("city") String city);

}
