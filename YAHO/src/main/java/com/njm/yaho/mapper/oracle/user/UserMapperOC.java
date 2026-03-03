package com.njm.yaho.mapper.oracle.user;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.njm.yaho.domain.oracle.user.UserOCDTO;

@Mapper
public interface UserMapperOC {
	int countByEmail(@Param("email") String email); // 이메일 중복 체크
	
	// 이메일로 ID 존재여부 찾기
	UserOCDTO findByEmail(@Param("email") String email);
	
	// ID와 이메일로 존재여부 찾기
	UserOCDTO findByIdAndEmail(@Param("userId") String userId, @Param("email") String email);
	
	// 임시비밀번호 발급 및 적용
	void updatePassword(@Param("userId") String userId, @Param("password") String password);
	
	int countTodayPwReset(@Param("userId") String userId);
	
	Timestamp findLastPwResetTime(@Param("userId") String userId);
	
	void insertPwResetLog(@Param("userId") String userId, @Param("email") String email);

	// 사용자 권한 확인
	int checkUserRole(String userId);
	
	// 사용자 프로필 이미지 가져오기
	String getUserProfileImg(String userId);
}
