package com.njm.yaho.service.main;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njm.yaho.domain.mysql.main.MainMSDTO;
import com.njm.yaho.domain.oracle.main.MainOCDTO;
import com.njm.yaho.mapper.mysql.main.MainMapperMS;
import com.njm.yaho.mapper.oracle.main.MainMapperOC;

@Service
public class MainServiceImpl implements MainService {
	@Autowired
	private MainMapperMS mapperMS;
	
	@Autowired
	private MainMapperOC mapperOC;
	
	@Override
	public List<MainMSDTO> getTodayAnimeList() {
		String[] koreanDays = { "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일" };
	    int dayIndex = LocalDate.now().getDayOfWeek().getValue() % 7; // 월:1 ~ 일:7
	    String today =  koreanDays[dayIndex - 1];
	    
		return mapperMS.getTodayAnimeList(today);
	}
	
	// 특정 애니 요약 정보 가져오기
	@Override
	public MainMSDTO getAnimeBaseInfo(int animeId) {
		MainMSDTO dto = mapperMS.getAnimeBaseInfo(animeId);
		return dto;
	}
	
	// 특정 애니 상세 정보 가져오기
	@Override
	public MainOCDTO getAnimeDetailInfo(int animeId) {
		MainOCDTO dto = mapperOC.getAnimeDetailInfo(animeId);
		return dto;
	}

	// 애니 랭킹 TOP10 가져오기
	@Override
	public List<MainMSDTO> selectTop10AnimeByScore() {
		return mapperMS.selectTop10AnimeByScore();
	}
}
