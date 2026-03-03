package com.njm.yaho.controller.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.njm.yaho.domain.oracle.main.VoteOCDTO;
import com.njm.yaho.service.main.VoteService;

@RestController
public class VoteController {
	@Autowired
	private VoteService voteService;
	
	static class VoteRequest {
        private int id;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }

	@PostMapping("/vote")
    public String vote(@RequestBody VoteRequest request) {
        int id = request.getId();
        int result = voteService.voteFor(id);

        if (result > 0) {
            return "success";
        } else {
            return "투표 중 문제가 발생하였습니다.";
        }
    }
	
	@GetMapping("/vote/resultData")
	@ResponseBody
	public List<VoteOCDTO> voteResultData() {
	    return voteService.getAllVotes(); // 전체 투표 결과를 JSON으로 리턴
	}

}
