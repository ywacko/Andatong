package com.unidt.bean;


public class TimeSpaneBean {
    String bi_name;
    int dt_year;
    int dt_month;
    String begin_date = null;
    String end_date = null;
    String venue = null;
    String name = null;
    int    type = 0; // 0 all; 1 单日； 2 双日； 3 周末； 4 工作日 ；

    public String getBi_name() {
        return bi_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBi_name(String bi_name) {
        this.bi_name = bi_name;
    }

    public int getDt_year() {
        return dt_year;
    }

    public void setDt_year(int dt_year) {
        this.dt_year = dt_year;
    }

    public int getDt_month() {
        return dt_month;
    }

    public void setDt_month(int dt_month) {
        this.dt_month = dt_month;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
