package com.njm.yaho.controller.main;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njm.yaho.controller.info.RateControllerTest;
import com.njm.yaho.domain.mysql.main.MainMSDTO;
import com.njm.yaho.domain.oracle.main.MainOCDTO;
import com.njm.yaho.mapper.oracle.user.UserMapperOC;
import com.njm.yaho.service.main.MainService;
import com.njm.yaho.service.main.VoteService;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpSession;

@Controller

@RequestMapping("/Main")
public class MainController {
	@Autowired
	private MainService service;
	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private UserMapperOC userMapperOC;
	
	private static final Logger log = LoggerFactory.getLogger(RateControllerTest.class);
  
	@GetMapping("")
	public String main(Model model,HttpSession session) {
		List<MainMSDTO> animeList = service.getTodayAnimeList();
		int animeCount = animeList.size();
		
		// 사용자 정보
		String USER_ID = (String)session.getAttribute("USER_ID");
		if (USER_ID != null) {
			int USER_PERMISSION = userMapperOC.checkUserRole(USER_ID);
			String USER_PROFILE_IMG = userMapperOC.getUserProfileImg(USER_ID);
			
			session.setAttribute("USER_PROFILE_IMG", USER_PROFILE_IMG);
			session.setAttribute("USER_PERMISSION", USER_PERMISSION);
			
			model.addAttribute("USER_PROFILE_IMG", session.getAttribute("USER_PROFILE_IMG"));
			model.addAttribute("USER_PERMISSION", session.getAttribute("USER_PERMISSION"));
		} else {
			model.addAttribute("USER_PERMISSION", 1);
		}
		
		
		model.addAttribute("USER_ID", USER_ID);
		model.addAttribute("animeCount", animeCount);
		model.addAttribute("animeList", animeList);
		
		Object playAudio = session.getAttribute("playAudio");
		if (playAudio != null && (boolean) playAudio) {
			model.addAttribute("playAudio", true);
	      	session.removeAttribute("playAudio"); // ✅ 재생 후 삭제
		}
		
		// 애니 랭킹 TOP10 가져오기
		List<MainMSDTO> animeListTop10 = service.selectTop10AnimeByScore();
		
		model.addAttribute("animeListTop10", animeListTop10);
		
		// 투표 불러오기
		//if (USER_ID != null) {
	    //    model.addAttribute("voteList", voteService.getAllVotes()); // 투표 리스트
	    //} else {
	    //    model.addAttribute("voteList", null); // 로그인 안된 경우 빈 값
	    //}
	
        return "Main/index";
    }
	
	// 요약정보 전송
	@GetMapping("/anime/baseInfo")
	@ResponseBody
	public MainMSDTO getAnimeBaseInfo( int animeId) {
		MainMSDTO dto = service.getAnimeBaseInfo(animeId);
		log.info("dto, animeId"+dto+animeId);
        return dto;
	}
	
	// 상세정보 전송
	@GetMapping("/anime/detailInfo")
	@ResponseBody
	public MainOCDTO getAnimeDetailInfo(int animeId) {
        MainOCDTO dto = service.getAnimeDetailInfo(animeId);
        return dto;
	}
}
