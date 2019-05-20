package com.kacela.prayer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayerActivity extends AppCompatActivity {

    int nowInMinutes;

    IPrayerService iPrayerService;
    IHijriService iHijriService;
    IDateService iDateService;
    TextView fajr, shurooq, duhr, asr, maghrib, isha;
    TextView nextP, timeR;
    TextView dayAr, monthAr, yearAr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        Date nowDate = new Date();
        int nowHours = nowDate.getHours();
        int nowMinutes = nowDate.getMinutes();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = df.format(nowDate);

        nowInMinutes = nowHours * 60 + nowMinutes;

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

        iPrayerService.getPrayer("oujda").enqueue(new Callback<Prayer>() {
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
}