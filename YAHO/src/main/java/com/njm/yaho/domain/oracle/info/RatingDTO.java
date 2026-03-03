package com.njm.yaho.domain.oracle.info;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class RatingDTO {

    private int SCORE_ID;

    @JsonProperty("ANIME_ID")
    private int ANIME_ID;

    @JsonProperty("SCORE_CONTENT")
    private String SCORE_CONTENT;

    @JsonProperty("SCORE_SCORE")
    private double SCORE_SCORE;

    @JsonProperty("USER_ID")
    private String USER_ID;
    
    @JsonProperty("SCORE_REGDATE")
    private String SCORE_REGDATE;
    
    @JsonProperty("USER_NICKNAME")
    private String USER_NICKNAME;
    
    @JsonProperty("USER_PROFILE_IMG")
    private String USER_PROFILE_IMG;
    
    private Integer COUNT;
}