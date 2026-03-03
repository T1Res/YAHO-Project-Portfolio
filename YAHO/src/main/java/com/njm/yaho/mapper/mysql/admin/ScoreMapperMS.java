package com.njm.yaho.mapper.mysql.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ScoreMapperMS {
	// MySQL: TBL_ANIME에 동기화
    @Update("UPDATE TBL_ANIME SET SCORE = #{score} WHERE ANIME_ID = #{animeId}")
    int updateMySQLScore(@Param("animeId") int animeId, @Param("score") double score);
}
