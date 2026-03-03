package com.njm.yaho.service.schedule;

import java.util.List;
import java.util.Map;

import com.njm.yaho.domain.mysql.schedule.ScheduleMSDTO;

public interface ScheduleService {
	// 방영중 애니 목록
	public Map<String, List<ScheduleMSDTO>> WeekdayAnimeList();
}
