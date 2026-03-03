package com.njm.yaho.service.main;

import java.util.List;

import com.njm.yaho.domain.mysql.main.MainMSDTO;
import com.njm.yaho.domain.oracle.main.MainOCDTO;

public interface MainService {
	// 오늘 방영중 애니 목록 가져오기
	public List<MainMSDTO> getTodayAnimeList();
	
	// 특정 애니 요약 정보 가져오기
	public MainMSDTO getAnimeBaseInfo(int animeId);
	
	// 특정 애니 상세 정보 가져오기
	public MainOCDTO getAnimeDetailInfo(int animeId);
	
	// 애니 랭킹 TOP10 가져오기
	public List<MainMSDTO> selectTop10AnimeByScore();
}
