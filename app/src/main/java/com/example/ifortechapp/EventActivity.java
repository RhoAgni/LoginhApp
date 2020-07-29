package com.example.ifortechapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class EventActivity extends AppCompatActivity {

    int SelectedDay;
    int SelectedMonth;
    int SelectedYear;
    int RoomID;

    ToggleButton DayButton;
    ToggleButton WeekButton;

    ArrayList<CalendarTag> Events;

    TableLayout PlanTable;

    ArrayList<TextView> Holders = new ArrayList<TextView>();

    int Segmentation = 15;

    String sessCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        SelectedDay = intent.getIntExtra("Day",0);
        SelectedMonth = intent.getIntExtra("Month",0);
        SelectedYear = intent.getIntExtra("Year",0);
        RoomID = intent.getIntExtra("RoomID", 0);
        sessCode = intent.getStringExtra("SessCode");
        PlanTable = (TableLayout) findViewById(R.id.TimeTable);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("Loaded Portrait");
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            System.out.println("Loaded Landscape");
        }

        Events = new ArrayList<CalendarTag>();
        GetData();

        String[] arraySpinner = new String[] {"5 Min", "10 Min", "15 Min", "30 Min"};
        Spinner segmentation = (Spinner) findViewById(R.id.Segmentation);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        segmentation.setAdapter(adapter);

        segmentation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ClearTable();
                if(position==0){
                    Segmentation=5;
                }
                if(position==1){
                    Segmentation=10;
                }
                if(position==2){
                    Segmentation=15;
                }
                if(position==3){
                    Segmentation=30;
                }
                FillTable(Segmentation);
                try{
                    EventsToTable();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        FillTable(Segmentation);

    }

    public void FillTable(int segmentation){
        float scale = getResources().getDisplayMetrics().density;
        for(int x=0; x<(1440/segmentation); x++){
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            newRow.setPadding((int) (5*scale + 0.5f),0,(int) (5*scale + 0.5f),0);
            //newRow.setBackgroundColor(getResources().getColor(R.color.time));

            TextView time = new TextView(this);
            time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,(int) (1*scale + 0.5f)));
            time.setText(Utility.MinuteToTime(x*segmentation));
            time.setBackgroundColor(getResources().getColor(R.color.time));
            time.setGravity(Gravity.CENTER);
            newRow.addView(time);

            TextView holder = new TextView(this);
            holder.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,(int) (1*scale + 0.5f)));
            holder.setBackgroundColor(getResources().getColor(R.color.holder));
            holder.setGravity(Gravity.CENTER);
            newRow.addView(holder);
            Holders.add(holder);

            PlanTable.addView(newRow);
        }
    }

    public void ClearTable(){
        PlanTable.removeAllViews();
        Holders.clear();
    }

    public void GetData(){
        try{
            System.out.println("Year : "+SelectedYear+" Month : "+SelectedMonth+" Day : "+SelectedDay);
            String date = Utility.FormatDate(SelectedYear, SelectedMonth, SelectedDay);
            int RoomID = this.RoomID;
            HashMap<String, String> dateData = new HashMap<String, String>();
            dateData.put("date",date);
            dateData.put("sesscode",sessCode);
            dateData.put("rid",Integer.toString(RoomID));
            System.out.println("Sent with rid : "+RoomID+" Sesscode : "+sessCode+" Date : "+date);
            Events = Utility.JsonToArray(ConnectionUtility.performDatePostCall(dateData,2));
            System.out.println("Found Array Size : "+Events.size());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void EventsToTable(){
        for(int x=0; x<Events.size(); x++){
            ColorTable((Events.get(x).StartHour*60)+Events.get(x).StartMinute,(Events.get(x).EndHour*60)+Events.get(x).EndMinute-(Events.get(x).StartHour*60)+Events.get(x).StartMinute, Color.parseColor(Events.get(x).color));
        }
    }

    public void ColorTable(int startMinute, int duration, int color){
        int IndexStart=(int)Math.ceil((double)startMinute/Segmentation);
        int IndexCount=(int)Math.ceil((double)duration/Segmentation);
        for(int x = IndexStart; x<IndexStart+IndexCount; x++){
            Holders.get(x).setBackgroundColor(color);
        }
    }

    public void BackButton(View view){
        finish();
    }
}
