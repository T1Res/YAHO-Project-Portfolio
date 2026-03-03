package com.njm.yaho.service.main;

import java.util.List;

import com.njm.yaho.domain.oracle.main.VoteOCDTO;

public interface VoteService {
	// 투표 결과 불러오기
	List<VoteOCDTO> getAllVotes();
	
	// 투표하기
    int voteFor(int voteId);
}
