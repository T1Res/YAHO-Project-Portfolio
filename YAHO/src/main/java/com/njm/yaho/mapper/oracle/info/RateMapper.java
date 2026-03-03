package com.njm.yaho.mapper.oracle.info;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.njm.yaho.domain.oracle.info.RatingChartDTO;
import com.njm.yaho.domain.oracle.info.RatingDTO;

@Mapper
public interface RateMapper {
    //댓글 등록
	int insertRate(RatingDTO dto);
    //댓글 리스트
    List<RatingDTO> selectRateListByAnime(int ANIME_ID);
    //평균 점수
    Double getAverageScore(int ANIME_ID);
    //스코어 갯수
    List<RatingDTO> selectRateCount(int ANIME_ID);
    //도넛 성별 리스트
    List<RatingChartDTO> selectGenderCount(int ANIME_ID);
    //댓글 수정
    int updateRate(RatingDTO dto);
    //댓글 검색
    RatingDTO searchRating(int ANIME_ID,String USER_ID);
    //댓글 삭제
    int deleteRate(int ANIME_ID,String USER_ID);
    
    int updateAnimeRate(double SCORE,int ANIME_ID);
}