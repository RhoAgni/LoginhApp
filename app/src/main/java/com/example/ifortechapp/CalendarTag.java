package com.example.ifortechapp;

import java.time.LocalTime;

public class CalendarTag{
    int mid;
    String subject;
    int StartHour;
    int StartMinute;
    int StartYear;
    int StartMonth;
    int StartDay;
    int EndHour;
    int EndMinute;
    int EndYear;
    int EndMonth;
    int EndDay;
    int uid;
    String color;
    String name;
    String surname;

    public String toString(){
        return "Mid : "+Integer.toString(mid)+" Subject : "+subject+" Start Time : "+Integer.toString(StartHour)+":"+Integer.toString(StartMinute)+" End Time : "+Integer.toString(EndHour)+":"+Integer.toString(EndMinute)+" UID : "+Integer.toString(uid)+" Color : "+color+" Name : "+name+" "+surname;
    }
}
