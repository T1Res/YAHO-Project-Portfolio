package com.njm.yaho.mapper.mysql.list;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.njm.yaho.domain.mysql.list.animeListMSDTO;

@Mapper
public interface ListMapperMS {
	// 전체 목록 (무한 스크롤)
	List<animeListMSDTO> selectAllAnimeScroll(@Param("limit") int limit, 
												@Param("offset") int offset,
												@Param("sort") String sort);

	List<String> selectAllTags();

	// 전체 목록 (필터링)
	List<animeListMSDTO> selectFilteredAnime(@Param("limit") int limit,
            @Param("offset") int offset,
            @Param("keyword") String keyword,
            @Param("tags") List<String> tags,
            @Param("sort") String sort);

}
