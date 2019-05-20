package com.kacela.prayer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kacela.prayer.model.Hijri;
import com.kacela.prayer.model.HijriData;
import com.kacela.prayer.model.Items;
import com.kacela.prayer.model.Prayer;
import com.kacela.prayer.model.PrayerData;
import com.kacela.prayer.remote.IHijriService;
import com.kacela.prayer.remote.IPrayerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayerActivity extends AppCompatActivity {

    int nowInMinutes;

    IPrayerService iPrayerService;
    IHijriService iHijriService;
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

                System.out.print(t.getMessage());
            }
        });

        iPrayerService.getPrayer("oujda").enqueue(new Callback<Prayer>() {
            @Override
            public void onResponse(Call<Prayer> call, Response<Prayer> response) {
                Items prayer = response.body().getItems()[0];

                fillTexts(prayer);

                convertItToDate(prayer);
            }

            @Override
            public void onFailure(Call<Prayer> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void fillTexts(Items prayer) {
        fajr.setText(prayer.getFajr());
        shurooq.setText(prayer.getShurooq());
        duhr.setText(prayer.getDhuhr());
        asr.setText(prayer.getAsr());
        maghrib.setText(prayer.getMaghrib());
        isha.setText(prayer.getIsha());
    }

    private void convertItToDate(Items prayer) {
        String strFajrDate = prayer.getFajr();
        String strShurooqDate = prayer.getShurooq();
        String strDuhrDate = prayer.getDhuhr();
        String strAsrDate = prayer.getAsr();
        String strMaghribDate = prayer.getMaghrib();
        String strIshaDate = prayer.getIsha();

        PrayerData p = new PrayerData("Default", "");
        p.setIntTime(nowInMinutes);

        PrayerData[] tabPrayerDataDate = {
            new PrayerData("Fajr", strFajrDate),
            new PrayerData("Shurooq", strShurooqDate),
            new PrayerData("Duhr", strDuhrDate),
            new PrayerData("Asr", strAsrDate),
            new PrayerData("Maghrib", strMaghribDate),
            new PrayerData("Isha", strIshaDate),
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