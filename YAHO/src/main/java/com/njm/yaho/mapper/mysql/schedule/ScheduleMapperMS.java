package com.njm.yaho.mapper.mysql.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.njm.yaho.domain.mysql.schedule.ScheduleMSDTO;

@Mapper
public interface ScheduleMapperMS {
	// 방영중 애니 목록
	public List<ScheduleMSDTO> WeekdayAnimeList();
}
