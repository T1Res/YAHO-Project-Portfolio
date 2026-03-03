package com.njm.yaho.service.list;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.njm.yaho.domain.mysql.list.animeListMSDTO;

@Service
public interface ListService {
	// 전체 목록 (무한 스크롤)
	List<animeListMSDTO> selectAllAnimeScroll(int page, int size, String sort);

	// 모든 태그 가져오기
	Map<String, Integer> getAllTagsWithCount();
	
	// 전체 목록 (필터링)
	List<animeListMSDTO> selectFilteredAnime(int page, int size, String keyword, List<String> tags, String sort);
}
