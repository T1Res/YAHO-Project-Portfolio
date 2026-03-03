package com.njm.yaho.domain.mysql.admin;

import lombok.Data;

@Data
public class AnimeMSDTO {
	private int ANIME_ID;
	private String TITLE;
	private String THUMNAIL_GARO_URL;
	private String THUMNAIL_SERO_URL;
	private String WEEKDAY;
	private double SCORE;
	private String TAGS;
	private String updated_at;

}
