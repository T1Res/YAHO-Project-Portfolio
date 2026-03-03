package com.njm.yaho.mapper.mysql.admin;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.njm.yaho.domain.mysql.admin.AnimeMSDTO;

@Mapper
public interface adminMapperMS {

	//등록
	void insertAnimeMS(AnimeMSDTO anime);

	//제목 불러오기
    List<AnimeMSDTO> getmslist();
    
    //수정
    void updateAnimeBasic(AnimeMSDTO animeBasicDTO);

    //삭제
    void deleteAnime(int animeId);
    
}
