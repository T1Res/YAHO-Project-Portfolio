package com.njm.yaho.service.admin;

import java.util.List;

import com.njm.yaho.domain.mysql.admin.AnimeMSDTO;
import com.njm.yaho.domain.oracle.admin.AnimeOCDTO;

public interface AnimeService {
	
	//등록 각각
    void insertAnimeMS(AnimeMSDTO anime);
    void insertAnimeOC(AnimeOCDTO anime);
    
    //등록 한 번에
	void insertFullAnime(AnimeMSDTO animeMS, AnimeOCDTO animeOC);

	//제목 불러오기
	List<AnimeMSDTO> getmslist();
	List<AnimeOCDTO> getoclist();
	
	//수정 각각
	void updateAnimeBasic(AnimeMSDTO animeBasiclDTO);
	void updateAnimeDetail(AnimeOCDTO animeDetailDTO);
	
	//삭제
	void deleteMS(int animeId);
	void deleteOC(int animeId);
	
	// 점수 동기화
	public void syncScore();
	
	// Oracle 점수 평균 계산
	public void scoreAveCal();
}
