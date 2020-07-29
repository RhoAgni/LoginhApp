package com.example.ifortechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.EventLog;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    int SelectedDay;
    int SelectedMonth;
    int SelectedYear;

    int RoomID;

    String SessionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        SelectedMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        SelectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        setContentView(R.layout.activity_home);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth){
                SelectedDay=dayOfMonth;
                SelectedMonth=month+1;
                SelectedYear=year;
            }
        });
        RoomID=3;
        SessionKey = getIntent().getStringExtra("SessCode");
        System.out.println("Logged In With Session COde : "+SessionKey);
    }

    public void Logout(View view){
        CurrentData data = (CurrentData) Utility.loadSettings("Current.dat", this);
        data.SessionKey="";
        data.StayConnected=false;
        Utility.saveSettings("Current.dat",data,this);
        finish();
    }

    public void SelectDate(View view){
        /*System.out.println("Year : "+SelectedYear+" Month : "+SelectedMonth+" Day : "+SelectedDay);
        String date = Utility.FormatDate(SelectedYear, SelectedMonth, SelectedDay);
        RoomID = 3;
        Data = (CurrentData) Utility.loadSettings("Current.dat", this);
        HashMap<String, String> dateData = new HashMap<String, String>();
        dateData.put("date",date);
        dateData.put("sesscode",Data.SessionKey);
        dateData.put("rid",Integer.toString(RoomID));
        System.out.println("Sent with rid : "+RoomID+" Sesscode : "+Data.SessionKey+" Date : "+date);*/
        Intent i = new Intent(HomeActivity.this, EventActivity.class);
        i.putExtra("Day",SelectedDay);
        i.putExtra("Month",SelectedMonth);
        i.putExtra("Year",SelectedYear);
        i.putExtra("RoomID",RoomID);
        i.putExtra("SessCode",SessionKey);
        startActivity(i);
    }
}
