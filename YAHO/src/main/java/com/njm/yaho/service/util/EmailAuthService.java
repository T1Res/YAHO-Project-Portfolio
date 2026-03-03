package com.njm.yaho.service.util;

import jakarta.servlet.http.HttpSession;

public interface EmailAuthService {
	boolean emailExistsInUser(String email);

	void sendAuthCode(String email);
	
	boolean verifyCode(String email, String inputCode, HttpSession session);
	
	void deleteByEmail(String email);
}
