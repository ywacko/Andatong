package com.unidt.helper.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static String getDateTime() {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date());
    }

    public static String getDate() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

    public static int getYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static void main(String[] args) {
        System.out.println(DateHelper.getDay());
    }
}
