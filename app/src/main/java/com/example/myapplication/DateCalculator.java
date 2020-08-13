package com.example.myapplication;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculator {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private String presentDate = simpleDateFormat.format(new Date());
    private String week[] =new String[7];

    public String[] getThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar.getAvailableLocales();
        for(int i=0;i<7;i++) {
            week[i] = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE,-1);

        }
        return week;
    }

    public String getPresentDate() {
        return presentDate;
    }
}
