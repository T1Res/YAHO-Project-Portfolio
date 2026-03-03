package com.njm.yaho.domain.mysql.list;

import lombok.Data;

@Data
public class animeListMSDTO {
	private int ANIME_ID;
	private String TITLE;
	private String THUMNAIL_GARO_URL;
	private String THUMNAIL_SERO_URL;
	private String WEEKDAY;
	private double SCORE;
	private String TAGS;
	private String UPDATED_AT;
}
