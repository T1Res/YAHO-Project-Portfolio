package com.njm.yaho.domain.oracle.admin;

import lombok.Data;

@Data
public class AnimeOCDTO {
	private int ANIME_ID;
	private String ANIME_DESC;
	private String START_DATE;
	private int TOTAL_EPISODE;
	private int CURRENT_EPISODE;
	private String STUDIO;
	private String STUDIO_LINK;
	private double ANIME_SCORE;
	private String AGE_RATING;
	private String CREATED_AT;
}
