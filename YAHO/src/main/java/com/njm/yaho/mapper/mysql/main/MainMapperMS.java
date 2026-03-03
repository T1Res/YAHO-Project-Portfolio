package com.njm.yaho.mapper.mysql.main;

import java.util.List;

import com.njm.yaho.domain.mysql.main.MainMSDTO;

public interface MainMapperMS {
	// 오늘 방영중 애니 목록 가져오기
	public List<MainMSDTO> getTodayAnimeList(String today);
	
	// 특정 애니 요약 정보 가져오기
	public MainMSDTO getAnimeBaseInfo(int animeId);
	
	// 애니 랭킹 TOP10 가져오기
	List<MainMSDTO> selectTop10AnimeByScore();
}
