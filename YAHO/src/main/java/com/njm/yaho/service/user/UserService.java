package com.njm.yaho.service.user;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.njm.yaho.domain.oracle.user.UserDTO;

@Service
public interface UserService {
	
	
	//로그인
	public UserDTO UserLogin(String USER_ID,String USER_PASSWORD);
	// 회원가입
	public int UserInsert(UserDTO dto);
	// 중복검사
	public UserDTO UserIdCheck(String USER_ID);
	//유저검색
	public UserDTO UserSearch(String USER_ID);
	//email중복
	public UserDTO UserEmailCheck(String USER_EMAIL);
	// 회원수정
	public void UserModify(UserDTO dto);
	// 회원탈퇴
	public void UserDelete(String USER_ID);
	// 프로필
	public UserDTO UserProfile(String USER_ID);
	public String saveProfileImage(MultipartFile file);
  
  // 비밀번호 리셋 서비스
  boolean resetPassword(String userId, String email);
}