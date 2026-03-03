package com.njm.yaho.service.schedule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njm.yaho.domain.mysql.schedule.ScheduleMSDTO;
import com.njm.yaho.mapper.mysql.schedule.ScheduleMapperMS;
import com.njm.yaho.mapper.oracle.schedule.ScheduleMapperOracle;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleMapperMS mapperMS;
	
	@Autowired
	private ScheduleMapperOracle mapperOracle;
	
	@Override
	public Map<String, List<ScheduleMSDTO>> WeekdayAnimeList() {
		List<ScheduleMSDTO> rawList = mapperMS.WeekdayAnimeList();
		List<ScheduleMSDTO> expandedList = new ArrayList<>();
		
		for (ScheduleMSDTO anime : rawList) {
		    String[] days = anime.getWEEKDAY().split(",");
		    for (String day : days) {
		        ScheduleMSDTO dto = new ScheduleMSDTO();
		        dto.setANIME_ID(anime.getANIME_ID());
		        dto.setTITLE(anime.getTITLE());
		        dto.setTHUMNAIL_GARO_URL(anime.getTHUMNAIL_GARO_URL());
		        dto.setTHUMNAIL_SERO_URL(anime.getTHUMNAIL_SERO_URL());
		        dto.setSCORE(anime.getSCORE());
		        dto.setTAGS(anime.getTAGS());
		        dto.setWEEKDAY(day.trim());
		        
		        expandedList.add(dto);
		    }
		}

		// 요일별 애니메이션을 그룹화
        Map<String, List<ScheduleMSDTO>> animeByDay = 
        		expandedList.stream().collect(
        				Collectors.groupingBy(
        						ScheduleMSDTO::getWEEKDAY, 
        						LinkedHashMap::new, Collectors.toList()));
		
		return animeByDay;
	}

}
