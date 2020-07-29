package com.example.ifortechapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean[] isPasswordValid(String password){
        boolean[] Checkers = new boolean[3];
        if(password.matches(".*\\d.*")){
            Checkers[0]=true;
        }
        else{
            Checkers[0]=false;
        }
        if(password.length()>8){
            Checkers[1]=true;
        }
        else{
            Checkers[1]=false;
        }
        String allUpper = password.toUpperCase();
        if(!allUpper.equals(password)){
            Checkers[2]=true;
        }
        else{
            Checkers[2]=false;
        }
        return Checkers;
    }

    public static void saveSettings(String fileName, Object object, Context context) {
        try{
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(object);
            os.close();
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Object loadSettings(String fileName, Context context){
        try{
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object objectSettings = is.readObject();
            is.close();
            fis.close();
            return objectSettings;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String FormatDate(int year, int month, int day){
        String selYear = Integer.toString(year);
        String selMonth;
        String selDay;
        if(month>9){
            selMonth=Integer.toString(month);
        }
        else{
            selMonth="0"+Integer.toString(month);
        }
        if(day>9){
            selDay=Integer.toString(day);
        }
        else{
            selDay="0"+Integer.toString(day);
        }
        return selYear+"-"+selMonth+"-"+selDay;
    }

    public static ArrayList<CalendarTag> JsonToArray(String string){
        ArrayList<CalendarTag> tags = new ArrayList<CalendarTag>();
        try{
            JSONArray jsonArray = new JSONArray(string);
            for(int x=0; x<jsonArray.length(); x++){
                JSONObject newObject = (JSONObject)jsonArray.get(x);
                tags.add(JsonToTag(newObject));
                System.out.println(tags.get(x).toString());;
            }
            return tags;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static CalendarTag JsonToTag(JSONObject object){
        CalendarTag tag = new CalendarTag();
        try{
            tag.mid = object.getInt("mid");
            tag.color = object.getString("color");
            tag.subject = object.getString("subject");
            tag.uid = object.getInt("uid");
            tag.name = object.getString("name");
            tag.surname = object.getString("surname");

            String start = object.getString("start_date");
            String[] startParts = start.split(" ");
            String startTime = startParts[1];
            String[] startTimeParts = startTime.split(":");
            tag.StartHour = Integer.parseInt(startTimeParts[0]);
            tag.StartMinute = Integer.parseInt(startTimeParts[1]);
            String startDay = startParts[0];
            String[] startDayParts = startDay.split("-");
            tag.StartYear = Integer.parseInt(startDayParts[0]);
            tag.StartMonth = Integer.parseInt(startDayParts[1]);
            tag.StartDay = Integer.parseInt(startDayParts[2]);

            String end = object.getString("end_date");
            String[] endParts = end.split(" ");
            String endTime = endParts[1];
            String[] endTimeParts = endTime.split(":");
            tag.EndHour = Integer.parseInt(endTimeParts[0]);
            tag.EndMinute = Integer.parseInt(endTimeParts[1]);
            String endDay = endParts[0];
            String[] endDayParts = endDay.split("-");
            tag.EndYear = Integer.parseInt(endDayParts[0]);
            tag.EndMonth = Integer.parseInt(endDayParts[1]);
            tag.EndDay = Integer.parseInt(endDayParts[2]);
        }
        catch(Exception e){}
        return tag;
    }

    public static String MinuteToTime(int Minutes){
        String time = "";
        int hour = Minutes/60;
        int remainingMinutes = Minutes-(hour*60);
        if(hour<10){
            time+="0"+Integer.toString(hour)+":";
        }
        else{
            time+=Integer.toString(hour)+":";
        }
        if(remainingMinutes<10){
            time+="0"+Integer.toString(remainingMinutes);
        }
        else{
            time+=Integer.toString(remainingMinutes);
        }
        return time;
    }

    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
