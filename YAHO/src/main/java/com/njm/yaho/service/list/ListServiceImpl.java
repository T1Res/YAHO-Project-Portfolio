package com.njm.yaho.service.list;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njm.yaho.domain.mysql.list.animeListMSDTO;
import com.njm.yaho.mapper.mysql.list.ListMapperMS;

@Service
public class ListServiceImpl implements ListService {
	// MySQL Mapper
	@Autowired
	private ListMapperMS mapperMS;
	
	// 전체 목록 (무한 스크롤)
	@Override
	public List<animeListMSDTO> selectAllAnimeScroll(int page, int size, String sort) {
		int offset = (page - 1) * size;
	    return mapperMS.selectAllAnimeScroll(size, offset, sort);
	}
	
	// 모든 태그 가져오기
	@Override
	public Map<String, Integer> getAllTagsWithCount() {
	    List<String> rawTags = mapperMS.selectAllTags();
	    Map<String, Integer> tagCountMap = new HashMap<>();

	    for (String tagLine : rawTags) {
	        if (tagLine != null) {
	            String[] tags = tagLine.split(",");
	            for (String tag : tags) {
	                tag = tag.trim();
	                if (!tag.isEmpty()) {
	                    tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
	                }
	            }
	        }
	    }

	    // 정렬된 Map 만들기 (LinkedHashMap 유지)
	    return tagCountMap.entrySet()
	            .stream()
	            .filter(entry -> entry.getValue() >= 2)
	            .sorted((e1, e2) -> e2.getValue() - e1.getValue()) // 등장 횟수 내림차순
	            .collect(Collectors.toMap(
	                    Map.Entry::getKey,
	                    Map.Entry::getValue,
	                    (e1, e2) -> e1,
	                    LinkedHashMap::new
	            ));
	}
	
	@Override
	public List<animeListMSDTO> selectFilteredAnime(int page, int size, String keyword, List<String> tags, String sort) {
		int offset = (page - 1) * size;
		return mapperMS.selectFilteredAnime(size, offset, keyword, tags, sort);
	}

}
