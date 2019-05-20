package com.kacela.prayer;

import com.kacela.prayer.remote.IDateService;
import com.kacela.prayer.remote.IHijriService;
import com.kacela.prayer.remote.IPrayerService;
import com.kacela.prayer.remote.RetrofitClient;

public class Common {

    private static final String BASE_URL = "http://muslimsalat.com/";
    private static final String BASE_URL_HIJRI = "http://api.aladhan.com/";
    //http://worldtimeapi.org/api/timezone/Africa/Casablanca.json
    private static final String BASE_URL_DATE = "http://worldtimeapi.org/api/timezone/";


    public static IPrayerService getPrayerService() {
        return RetrofitClient.getClient(BASE_URL).create(IPrayerService.class);
    }


    public static IHijriService getHijriService() {
        return RetrofitClient.getClient(BASE_URL_HIJRI).create(IHijriService.class);
    }


    public static IDateService getDateService() {
        return RetrofitClient.getClient(BASE_URL_DATE).create(IDateService.class);
    }

}
