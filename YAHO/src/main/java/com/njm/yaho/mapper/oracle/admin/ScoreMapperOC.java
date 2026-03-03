package com.njm.yaho.mapper.oracle.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ScoreMapperOC {
	// TBL_SCORE에서 사용된 ANIME_ID 목록 전부 조회
    @Select("SELECT DISTINCT ANIME_ID FROM TBL_SCORE")
    List<Integer> getAllAnimeIds();

    // 평균 평점 계산
    @Select("SELECT ROUND(AVG(SCORE_SCORE), 1) FROM TBL_SCORE WHERE ANIME_ID = #{animeId}")
    Double calAverageScoreByAnimeId(@Param("animeId") int animeId);
    
    // 평균 평점 불러오기
    @Select("SELECT ANIME_SCORE FROM TBL_ANIME WHERE ANIME_ID = #{animeId}")
    Double getAverageScoreByAnimeId(@Param("animeId") int animeId);

    // Oracle: TBL_ANIME에 평균 반영
    @Update("UPDATE TBL_ANIME SET ANIME_SCORE = #{score} WHERE ANIME_ID = #{animeId}")
    int updateAnimeScore(@Param("animeId") int animeId, @Param("score") double score);
}
