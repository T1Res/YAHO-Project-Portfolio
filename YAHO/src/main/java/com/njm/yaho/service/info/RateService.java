package com.njm.yaho.service.info;

import java.util.List;

import com.njm.yaho.domain.oracle.info.RatingChartDTO;
import com.njm.yaho.domain.oracle.info.RatingDTO;

public interface RateService {
	int insertRate(RatingDTO dto);
	
	List<RatingDTO> selectRateListByAnime(int ANIME_ID);
    
	Double getAverageScore(int ANIME_ID);
    
    RatingDTO getRate(long mno);
    
    List<RatingDTO> selectRateCount(int ANIME_ID);
    
    List<RatingChartDTO> selectGenderCount(int ANIME_ID);
    
    //댓글 수정
    int updateRate(RatingDTO dto);
    //댓글 검색
    RatingDTO searchRating(int ANIME_ID,String USER_ID);
    //댓글 삭제
    int deleteRate(int ANIME_ID,String USER_ID);
    
    int updateAnimeRate(double SCORE,int ANIME_ID);
    
}
