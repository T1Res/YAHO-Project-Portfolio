package com.njm.yaho.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.njm.yaho.domain.MergeDTO;
import com.njm.yaho.domain.mysql.admin.AnimeMSDTO;
import com.njm.yaho.domain.oracle.admin.AnimeOCDTO;
import com.njm.yaho.service.admin.AnimeService;

@Controller
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AnimeService animeService;

  
    @PostMapping("/insert_full")
    public String insertFullAnime(
        @RequestParam String TITLE,
        @RequestParam(required = false) MultipartFile THUMNAIL_GARO_URL,
        @RequestParam(required = false) MultipartFile THUMNAIL_SERO_URL,
        @RequestParam(required = false) String[] WEEKDAY,
        @RequestParam double SCORE,
        @RequestParam String TAGS,

        // Oracle용
        @RequestParam String ANIME_DESC,
        @RequestParam String START_DATE,
        @RequestParam int TOTAL_EPISODE,
        @RequestParam int CURRENT_EPISODE,
        @RequestParam String STUDIO,
        @RequestParam String STUDIO_LINK,
        @RequestParam String AGE_RATING
    ) {
        AnimeMSDTO animeMS = new AnimeMSDTO();
        animeMS.setTITLE(TITLE);

        String uploadGARO = System.getProperty("user.dir") + "/src/main/resources/static/IMG/ANIME/GARO/";
        String uploadSERO = System.getProperty("user.dir") + "/src/main/resources/static/IMG/ANIME/SERO/";

        try {
            if (THUMNAIL_GARO_URL != null && !THUMNAIL_GARO_URL.isEmpty()) {
                File saveFile = new File(uploadGARO + THUMNAIL_GARO_URL.getOriginalFilename());
                THUMNAIL_GARO_URL.transferTo(saveFile);
                animeMS.setTHUMNAIL_GARO_URL("/IMG/ANIME/GARO/" + THUMNAIL_GARO_URL.getOriginalFilename());
            }

            if (THUMNAIL_SERO_URL != null && !THUMNAIL_SERO_URL.isEmpty()) {
                File saveFile = new File(uploadSERO + THUMNAIL_SERO_URL.getOriginalFilename());
                THUMNAIL_SERO_URL.transferTo(saveFile);
                animeMS.setTHUMNAIL_SERO_URL("/IMG/ANIME/SERO/" + THUMNAIL_SERO_URL.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        if (WEEKDAY != null) {
            animeMS.setWEEKDAY(String.join(",", WEEKDAY));
        } else {
            animeMS.setWEEKDAY("");
        }

        animeMS.setSCORE(SCORE);
        animeMS.setTAGS(TAGS);

        // MySQL 저장 → 생성된 ID 받아오기
        animeService.insertAnimeMS(animeMS); // ID가 자동으로 설정된다면
        int generatedAnimeId = animeMS.getANIME_ID(); // insert 이후 ID 리턴되게 설정 필요

        // Oracle 저장
        AnimeOCDTO animeOC = new AnimeOCDTO();
        animeOC.setANIME_ID(generatedAnimeId); // 위에서 받은 ID 사용
        animeOC.setANIME_DESC(ANIME_DESC);
        animeOC.setSTART_DATE(START_DATE);
        animeOC.setTOTAL_EPISODE(TOTAL_EPISODE);
        animeOC.setCURRENT_EPISODE(CURRENT_EPISODE);
        animeOC.setSTUDIO(STUDIO);
        animeOC.setSTUDIO_LINK(STUDIO_LINK);
        animeOC.setANIME_SCORE(SCORE);
        animeOC.setAGE_RATING(AGE_RATING);

        animeService.insertAnimeOC(animeOC);

        return "redirect:/Admin/list";
    }

    
    
    @GetMapping("/list")
    public String animeList(Model model) {
        // 1. 기존 리스트 가져오기
        List<AnimeMSDTO> mslist = animeService.getmslist();
        List<AnimeOCDTO> oclist = animeService.getoclist();

        // 2. 병합 리스트 생성
        List<MergeDTO> mergedList = new ArrayList();

        for (AnimeMSDTO ms : mslist) {
        	MergeDTO merged = new MergeDTO();
            merged.setMs(ms);

            for (AnimeOCDTO oc : oclist) {
                if (ms.getANIME_ID()==(oc.getANIME_ID())) {
                	String sd = oc.getSTART_DATE();
                	String yy, mm, dd;
                	yy = sd.substring(0,4);
                	mm = sd.substring(5,7);
                	dd = sd.substring(8,10);
                	String date = yy+mm+dd;
                	oc.setSTART_DATE(date);
                    merged.setOc(oc);
                    break;
                }
            }

            mergedList.add(merged);
        }

        // 3. 뷰에 병합 리스트 전달
        model.addAttribute("mergedList", mergedList);
        return "Admin/anime_list.html";
    }

    
    @PostMapping("/edit_basic")
    public String editAnimeBasic(@RequestParam("ANIME_ID") int animeId,
                                  @RequestParam("TITLE") String title,
                                  @RequestParam("SCORE") double score,
                                  @RequestParam(required = false) MultipartFile THUMNAIL_GARO_URL,
                                  @RequestParam(required = false) MultipartFile THUMNAIL_SERO_URL,
                                  @RequestParam("TAGS") String tags,
                                  @RequestParam("BASIC-WEEKDAY") String[] weekdays) {
        AnimeMSDTO anime = new AnimeMSDTO();
        
        String uploadGARO = System.getProperty("user.dir") + "/src/main/resources/static/IMG/ANIME/GARO/";
        String uploadSERO = System.getProperty("user.dir") + "/src/main/resources/static/IMG/ANIME/SERO/";

        try {
            if (THUMNAIL_GARO_URL != null && !THUMNAIL_GARO_URL.isEmpty()) {
                File saveFile = new File(uploadGARO + THUMNAIL_GARO_URL.getOriginalFilename());
                THUMNAIL_GARO_URL.transferTo(saveFile);
                anime.setTHUMNAIL_GARO_URL("/IMG/ANIME/GARO/" + THUMNAIL_GARO_URL.getOriginalFilename());
            }

            if (THUMNAIL_SERO_URL != null && !THUMNAIL_SERO_URL.isEmpty()) {
                File saveFile = new File(uploadSERO + THUMNAIL_SERO_URL.getOriginalFilename());
                THUMNAIL_SERO_URL.transferTo(saveFile);
                anime.setTHUMNAIL_SERO_URL("/IMG/ANIME/SERO/" + THUMNAIL_SERO_URL.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        
        // 파라미터들을 각각 @RequestParam으로 받음
        anime.setANIME_ID(animeId);
        anime.setTITLE(title);
        anime.setSCORE(score);
        anime.setTAGS(tags);
        
        // 요일 배열로 받기
        String weekdayString = String.join(",", weekdays); // "월,화,수" 이렇게 만들기
        anime.setWEEKDAY(weekdayString);
        
        animeService.updateAnimeBasic(anime);
        return "redirect:/Admin/list";
    }
    
    @PostMapping("/edit_detail")
    public String editAnimeDetail(@ModelAttribute AnimeOCDTO anime) {
        animeService.updateAnimeDetail(anime);
        return "redirect:/Admin/list"; // 수정 완료 후 이동할 페이지
    }

    @PostMapping("/deleteAnime")
    public String deleteAnime(@RequestParam("animeId") int animeId) {
    	System.out.print(animeId);
        animeService.deleteMS(animeId);
        animeService.deleteOC(animeId);
        return "redirect:/Admin/list"; // 삭제 후 애니 리스트로 리다이렉트
    }
    
    // ★ 추가: 애니메이션 평점 수동 갱신 버튼 처리
    @PostMapping("/syncScore")
    @ResponseBody
    public String syncScoreManually() {
        animeService.syncScore(); // Oracle 평균 계산 + MySQL 동기화 수행
        return "평점 갱신 완료";
    }
}

