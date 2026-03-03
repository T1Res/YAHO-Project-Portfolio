package com.njm.yaho.domain.oracle.main;

@lombok.Data
public class MainOCDTO {
    private int animeId;
    private String animeDesc;
    private String startDate;
    private int totalEpisode;
    private int currentEpisode;
    private String studio;
    private String studioLink;
    private double animeScore;
    private String ageRating;
    private String createdAt;
}
