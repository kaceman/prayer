package com.kacela.prayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kacela.prayer.model.CurrentDate;
import com.kacela.prayer.model.Hijri;
import com.kacela.prayer.model.HijriData;
import com.kacela.prayer.model.Items;
import com.kacela.prayer.model.Prayer;
import com.kacela.prayer.model.PrayerData;
import com.kacela.prayer.remote.IDateService;
import com.kacela.prayer.remote.IHijriService;
import com.kacela.prayer.remote.IPrayerService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayerActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private LocationListener listener;

    int nowInMinutes;

    IPrayerService iPrayerService;
    IHijriService iHijriService;
    IDateService iDateService;
    TextView fajr, shurooq, duhr, asr, maghrib, isha;
    TextView nextP, timeR;
    TextView dayAr, monthAr, yearAr;

    TextView city_text;
    ImageButton refresh_btn, location_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);


        iPrayerService = Common.getPrayerService();
        iHijriService = Common.getHijriService();
        iDateService = Common.getDateService();
        fajr = findViewById(R.id.fajrTxt);
        shurooq = findViewById(R.id.shurooqTxt);
        duhr = findViewById(R.id.duhrTxt);
        asr = findViewById(R.id.asrTxt);
        maghrib = findViewById(R.id.maghribTxt);
        isha = findViewById(R.id.ishaTxt);

        nextP = findViewById(R.id.nextPTxt);
        timeR = findViewById(R.id.timeRTxt);

        dayAr = findViewById(R.id.dayArTxt);
        monthAr = findViewById(R.id.monthArTxt);
        yearAr = findViewById(R.id.yearArTxt);

        city_text = findViewById(R.id.city_text);
        refresh_btn = findViewById(R.id.refresh_button);
        location_btn = findViewById(R.id.location_button);



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.getLatitude() + ", " + location.getLongitude());
                fetch_data(location);
                locationManager.removeUpdates(listener);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();

        fetch_location();
    }

    private void fillTexts(final Items prayer, final String timezone) {
        String timeZId = Calendar.getInstance().getTimeZone().getID();
        iDateService.getDate(timeZId).enqueue(new Callback<CurrentDate>() {
            @Override
            public void onResponse(Call<CurrentDate> call, Response<CurrentDate> response) {
                String timeZ = response.body().getUtcOffset();
                int diff = timeZoneDiff(timeZ, timezone);

                fajr.setText(getTime(prayer.getFajr(), diff));
                shurooq.setText(getTime(prayer.getShurooq(), diff));
                duhr.setText(getTime(prayer.getDhuhr(), diff));
                asr.setText(getTime(prayer.getAsr(), diff));
                maghrib.setText(getTime(prayer.getMaghrib(), diff));
                isha.setText(getTime(prayer.getIsha(), diff));

                convertItToDate(prayer, diff);
            }

            @Override
            public void onFailure(Call<CurrentDate> call, Throwable t) {
                Log.e("iDateService", t.getMessage());
            }
        });
    }

    private String getTime(String time, int diff) {
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("hh:ss a", Locale.ENGLISH);
            Date d = format.parse(time);
            cal.setTime(d);
            cal.add(Calendar.HOUR, diff);
            Date oneHourBack = cal.getTime();
            DateFormat format24 = new SimpleDateFormat("HH:ss", Locale.ENGLISH);
            return format24.format(oneHourBack);
        } catch (Exception e) {
            Log.e("getTime", e.getMessage());
        }
        return "00:00";
    }

    private int timeZoneDiff(String timezone, String time) {
        char signal = timezone.charAt(0);
        String timeFull = timezone.substring(1);
        String[] parts = timeFull.split(":");
        int timeZ = Integer.parseInt(parts[0]);
        if (signal == '-') {
            timeZ *= -1;
        }

        return timeZ - Integer.parseInt(time);
    }

    private void convertItToDate(Items prayer, int diff) {
        String strFajrDate = prayer.getFajr();
        String strShurooqDate = prayer.getShurooq();
        String strDuhrDate = prayer.getDhuhr();
        String strAsrDate = prayer.getAsr();
        String strMaghribDate = prayer.getMaghrib();
        String strIshaDate = prayer.getIsha();

        PrayerData p = new PrayerData("Default", "");
        p.setIntTime(nowInMinutes);

        PrayerData[] tabPrayerDataDate = {
                new PrayerData("Fajr", getTime(strFajrDate, diff)),
                new PrayerData("Shurooq", getTime(strShurooqDate, diff)),
                new PrayerData("Duhr", getTime(strDuhrDate, diff)),
                new PrayerData("Asr", getTime(strAsrDate, diff)),
                new PrayerData("Maghrib", getTime(strMaghribDate, diff)),
                new PrayerData("Isha", getTime(strIshaDate, diff)),
                p
        };

        displayNextPrayer(tabPrayerDataDate, p);
    }

    private void displayNextPrayer(PrayerData[] tabPrayerDataDate, PrayerData p) {
        Arrays.sort(tabPrayerDataDate, new Comparator<PrayerData>() {
            @Override
            public int compare(PrayerData t1, PrayerData t2) {
                return Integer.compare(t1.getIntTime(), t2.getIntTime());
            }
        });

        int indexOfNow = Arrays.asList(tabPrayerDataDate).indexOf(p);

        int diff;
        String namePrayer;

        if (indexOfNow == 6) {
            diff = tabPrayerDataDate[0].getIntTime() + 24 * 60 - tabPrayerDataDate[indexOfNow].getIntTime();
            namePrayer = tabPrayerDataDate[0].getName();
        } else {
            diff = tabPrayerDataDate[indexOfNow + 1].getIntTime() - tabPrayerDataDate[indexOfNow].getIntTime();
            namePrayer = tabPrayerDataDate[indexOfNow + 1].getName();
        }

        nextP.setText(namePrayer);
        timeR.setText(timeToStr(diff));
    }

    @NonNull
    private String timeToStr(int time) {
        int hours = time / 60;
        int minutes = time - hours * 60;
        String strHours = hours < 10 ? "0" + hours : String.valueOf(hours);
        String strMinutes = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        return strHours + ":" + strMinutes;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button(); break;
            default:
                break;
        }
    }

    void configure_button() {

        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch_location();
            }
        });
    }

    void fetch_location() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }

    void fetch_data(Location location) {
        Date nowDate = new Date();
        int nowHours = nowDate.getHours();
        int nowMinutes = nowDate.getMinutes();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = df.format(nowDate);

        nowInMinutes = nowHours * 60 + nowMinutes;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String cityName = addresses.get(0).getLocality();

            city_text.setText(cityName);

            iPrayerService.getPrayer(cityName).enqueue(new Callback<Prayer>() {
                @Override
                public void onResponse(Call<Prayer> call, Response<Prayer> response) {
                    Items prayer = response.body().getItems()[0];
                    String timezone = response.body().getTimezone();

                    fillTexts(prayer, timezone);
                }

                @Override
                public void onFailure(Call<Prayer> call, Throwable t) {
                    Log.e("iPrayerService", t.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        iHijriService.getHijri(todayDate).enqueue(new Callback<HijriData>() {
            @Override
            public void onResponse(Call<HijriData> call, Response<HijriData> response) {

                Hijri h = response.body().getData().getHijri();
                String month = h.getMonth().getAr();
                String day = h.getDay();
                String year = h.getYear();

                dayAr.setText(day);
                monthAr.setText(month);
                yearAr.setText(year);
            }

            @Override
            public void onFailure(Call<HijriData> call, Throwable t) {
                Log.e("iHijriService", t.getMessage());
            }
        });
    }
}