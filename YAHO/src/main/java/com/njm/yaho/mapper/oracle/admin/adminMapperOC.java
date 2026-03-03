package com.njm.yaho.mapper.oracle.admin;

import com.njm.yaho.domain.oracle.admin.AnimeOCDTO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface adminMapperOC {
	
	//등록
	void insertAnimeOC(AnimeOCDTO anime);
    
	//제목 불러오기
	List<AnimeOCDTO> getoclist();
	
	//수정
	void updateAnimeDetail(AnimeOCDTO animeDetailDTO);

	//삭제
	void deleteAnime(int animeId);
	
}
