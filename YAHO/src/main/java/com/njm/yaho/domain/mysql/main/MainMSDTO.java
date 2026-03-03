package com.njm.yaho.domain.mysql.main;

@lombok.Data
public class MainMSDTO {
    private int animeId;
    private String title;
    private String thumnailGaroUrl;  // ← 필드명 소문자 + camelCase
    private String thumnailSeroUrl;
    private String weekday;
    private double score;
    private String tags;

    public String getWeekday() {
        return weekday != null ? weekday.trim() : null;
    }
}
