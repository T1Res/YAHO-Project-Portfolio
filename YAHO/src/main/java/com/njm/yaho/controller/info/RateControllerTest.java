package com.njm.yaho.controller.info;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.njm.yaho.domain.oracle.info.RatingChartDTO;
import com.njm.yaho.domain.oracle.info.RatingDTO;
import com.njm.yaho.service.info.RateService;

@Controller
@RequestMapping("/Info")
public class RateControllerTest {
	private static final Logger log = LoggerFactory.getLogger(RateControllerTest.class);

	@Autowired
	private RateService service;

	@GetMapping("/rate_test")
	public String rate_test() {
		return "Info/rate_test";
	}

	@GetMapping("/test2")
	public String rate_test2() {
		return "Info/rate_test2";
	}

	@PostMapping("/submitTest")
	public String submitTest(RatingDTO dto, Model model) {
		log.info("dto.SCORE_ID : " + dto.getSCORE_ID());
		log.info("dto.ANIME_ID : " + dto.getANIME_ID());
		log.info("dto.SCORE_CONTENT : " + dto.getSCORE_CONTENT());
		log.info("dto.SCORE_SCORE : " + dto.getSCORE_SCORE());
		log.info("dto.USER_ID : " + dto.getUSER_ID());

		int row = service.insertRate(dto);
		log.info("데이터 등록" + row);
		return "redirect:/Schedule/animeSchedule";
	}

	@GetMapping("/rate_inven")
	public String rate_inven(Model model) {
		int anime_id = 999;
		List<RatingDTO> list = service.selectRateListByAnime(999);
		log.info("리스트 가져오기 완료" + list);
		model.addAttribute("list", list);
		return "Info/rate_inven";
	}

	@GetMapping("/testChart")
	public String testChart() {
		return "Info/testChart";
	}

	@GetMapping("/testChart2")
	public String testChart2(Model model) {
		List<RatingDTO> ratingList = service.selectRateCount(999);

		// 5개의 구간 정의: 0~1, 1~2, 2~3, 3~4, 4~5
		List<String> scoreLabels = Arrays.asList("0~1", "1~2", "2~3", "3~4", "4~5");
		int[] countPerRange = new int[5];

		for (RatingDTO dto : ratingList) {
			double score = dto.getSCORE_SCORE();
			int count = dto.getCOUNT();

			if (score >= 0 && score < 1) {
				countPerRange[0] += count;
			} else if (score >= 1 && score < 2) {
				countPerRange[1] += count;
			} else if (score >= 2 && score < 3) {
				countPerRange[2] += count;
			} else if (score >= 3 && score < 4) {
				countPerRange[3] += count;
			} else if (score >= 4 && score <= 5) {
				countPerRange[4] += count;
			}
		}

		// 리스트로 변환하여 모델에 전달
		List<Integer> countList = Arrays.stream(countPerRange).boxed().collect(Collectors.toList());

		model.addAttribute("scoreList", scoreLabels); // X축 레이블
		model.addAttribute("countList", countList); // Y축 값

		log.info("X축 구간: " + scoreLabels);
		log.info("Y축 count: " + countList);

		return "Info/testChart2";
	}

	/*
	 * @GetMapping("/genderBar") public String genderBar(Model model) { double
	 * maleRatio = 40.0; // 백분율 double femaleRatio = 60.0;
	 * 
	 * model.addAttribute("maleRatio", maleRatio); model.addAttribute("femaleRatio",
	 * femaleRatio); return "Info/genderBar"; }
	 */
	@GetMapping("/genderChart")
	public void genderChart(Model model) {

		List<RatingChartDTO> RClist = service.selectGenderCount(999);
		log.info("도넛 차트 리스트: " + RClist);

		if (RClist != null && !RClist.isEmpty()) {
			RatingChartDTO dto = RClist.get(0);
			int menCount = dto.getMENCOUNT();
			int girlCount = dto.getGIRLCOUNT();

			int total = menCount + girlCount;
			double maleRatio = total > 0 ? Math.round((menCount * 100.0 / total) * 10.0) / 10.0 : 0.0;
			double femaleRatio = total > 0 ? Math.round((girlCount * 100.0 / total) * 10.0) / 10.0 : 0.0;

			model.addAttribute("maleRatio", maleRatio);
			model.addAttribute("femaleRatio", femaleRatio);

			log.info("남자: " + maleRatio);
			log.info("여자: " + femaleRatio);
		} else {
			log.info("RClist가 비어있다");
			model.addAttribute("maleRatio", 0);
			model.addAttribute("femaleRatio", 0);
		}

	}

	@GetMapping("/updateRate")
	public String updateRate(Model model, String USER_ID) {
	    int anime_id = 999;
	    List<RatingDTO> fullList = service.selectRateListByAnime(anime_id);

	    RatingDTO matchedDto = null;

	    // USER_ID가 유효할 때만 필터링
	    if (USER_ID != null && !USER_ID.trim().isEmpty()) {
	        String trimmedId = USER_ID.trim();

	        // 일치하는 DTO 찾기
	        for (RatingDTO dto : fullList) {
	            if (trimmedId.equals(dto.getUSER_ID())) {
	                matchedDto = dto;
	                model.addAttribute("Aluser_ID", trimmedId);
	                model.addAttribute("Aldto", dto);
	                log.info("특정 유저아이디 dto 확인: " + trimmedId + " / " + dto);
	                break;
	            }
	        }

	        // 리스트에서 일치하는 DTO 제거
	        if (matchedDto != null) {
	            fullList.remove(matchedDto);
	        }
	    }

	    model.addAttribute("list", fullList);
	    return "Info/updateRate";
	}
	@PostMapping("/updateRate")
	public String updateRateGet(Model model,RatingDTO dto) {
		RatingDTO rdto = service.searchRating(dto.getANIME_ID(), dto.getUSER_ID());
		log.info("아니메 아이디 : "+dto.getANIME_ID());
		log.info("유저 아이디 : "+dto.getUSER_ID());
		
		model.addAttribute("dto",rdto);
		return"Info/updateRatePro";
	}
	@PostMapping("/updateRatePro")
	public String updateRatePro(Model model,RatingDTO dto) {
		int row = service.updateRate(dto);
		log.info("row:"+row);
		log.info("컨텐츠:"+dto.getSCORE_CONTENT());
		model.addAttribute("dto",dto);
		return"redirect:/Schedule/animeSchedule";
	}
	
	
}
