package com.njm.yaho.mapper.oracle.main;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.njm.yaho.domain.oracle.main.VoteOCDTO;

@Mapper
public interface VoteMapperOC {
	// 전체 투표 리스트 가져오기
    List<VoteOCDTO> selectAllVote();

    // 특정 ID에 대해 VOTE_COUNT 증가
    int updateVoteCount(int voteId);
}
