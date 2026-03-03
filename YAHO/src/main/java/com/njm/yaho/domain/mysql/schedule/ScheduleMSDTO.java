package com.njm.yaho.domain.mysql.schedule;

@lombok.Data
public class ScheduleMSDTO {
	private int ANIME_ID;
	private String TITLE;
	private String THUMNAIL_GARO_URL;
	private String THUMNAIL_SERO_URL;
	private String WEEKDAY;
	private double SCORE;
	private String TAGS;
    
    public String getWEEKDAY() {
        return WEEKDAY.trim();
    }
}
