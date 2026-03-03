package com.njm.yaho.controller.list;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njm.yaho.domain.mysql.list.animeListMSDTO;
import com.njm.yaho.service.list.ListService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("List")
public class ListController {
	private static Logger log = LoggerFactory.getLogger(ListController.class);
	
	@Autowired
	private ListService listService;

	
	// 전체 목록 (최초 로드)
	@GetMapping
    public String animeList(Model model,@ModelAttribute("playAudio") Object flashAudio,HttpSession session) {
		// 초기 목록
        model.addAttribute("list", listService.selectAllAnimeScroll(1, 40, "asc"));
        // 모든 태그 가져오기
        model.addAttribute("tagMap", listService.getAllTagsWithCount()); // 태그 + 개수
        
        log.info("세션 로그인 확인"+(String)session.getAttribute("USER_ID"));
        String USER_ID = (String)session.getAttribute("USER_ID");
		model.addAttribute("USER_ID", USER_ID);
        return "List/list.html";
    }
	
	// 전체 목록 (무한 스크롤, 검색, 태그, 정렬)
	@GetMapping("/animeList")
	public String loadMore(
	    @RequestParam(value = "page", defaultValue = "1") int page,
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "tag", required = false) List<String> tags,
	    @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
	    Model model) {

	    List<animeListMSDTO> result;

	    if ((tags == null || tags.isEmpty()) && (keyword == null || keyword.isEmpty())) {
	        result = listService.selectAllAnimeScroll(page, 40, sort);
	    } else {
	        result = listService.selectFilteredAnime(page, 40, keyword, tags, sort);
	    }
	    
	    model.addAttribute("tagMap", extractTagMapFromResults(result, tags));
	    model.addAttribute("list", result);
	    return "List/list.html :: animeCardsFragment";
	}
	
	// 태그 필터링
	@GetMapping("/tagListFragment")
	public String getFilteredTagList(
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "tag", required = false) List<String> tags,
	    Model model) {

	    // 필터링된 애니메이션 리스트 기준으로 태그 다시 계산
	    List<animeListMSDTO> filteredList = listService.selectFilteredAnime(1, Integer.MAX_VALUE, keyword, tags, "asc");
	    model.addAttribute("tagMap", extractTagMapFromResults(filteredList, tags));
	    model.addAttribute("selectedTags", tags);

	    return "List/list.html :: tagListFragment";
	}

	
	// 태그 제한 메서드
	private Map<String, Integer> extractTagMapFromResults(List<animeListMSDTO> animeList, List<String> selectedTags) {
	    Map<String, Integer> tagMap = new HashMap<>();
	    for (animeListMSDTO anime : animeList) {
	        if (anime.getTAGS() != null) {
	            String[] tags = anime.getTAGS().split(",");
	            for (String tag : tags) {
	                tag = tag.trim();
	                if (!tag.isEmpty()) {
	                    tagMap.put(tag, tagMap.getOrDefault(tag, 0) + 1);
	                }
	            }
	        }
	    }
	    return tagMap.entrySet().stream()
	            .sorted((a, b) -> {
	                boolean aSelected = selectedTags != null && selectedTags.contains(a.getKey());
	                boolean bSelected = selectedTags != null && selectedTags.contains(b.getKey());

	                if (aSelected && bSelected) {
	                    // 선택된 태그는 선택된 순서로 정렬
	                    return Integer.compare(selectedTags.indexOf(a.getKey()), selectedTags.indexOf(b.getKey()));
	                }
	                if (aSelected) return -1; // a가 선택된 태그 → 우선
	                if (bSelected) return 1;  // b가 선택된 태그 → 우선
	                return Integer.compare(b.getValue(), a.getValue()); // 그 외는 인기순
	            })
	            .collect(Collectors.toMap(
	                Map.Entry::getKey,
	                Map.Entry::getValue,
	                (e1, e2) -> e1,
	                LinkedHashMap::new
	            ));
	    }
}
