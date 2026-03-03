package com.njm.yaho.service.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njm.yaho.domain.oracle.main.VoteOCDTO;
import com.njm.yaho.mapper.oracle.main.VoteMapperOC;

@Service
public class VoteServiceImpl implements VoteService {
	@Autowired
    private VoteMapperOC mapperOC;
	
	@Override
	public List<VoteOCDTO> getAllVotes() {
		return mapperOC.selectAllVote();
	}

	@Override
	public int voteFor(int voteId) {
		return mapperOC.updateVoteCount(voteId);
	}

}
