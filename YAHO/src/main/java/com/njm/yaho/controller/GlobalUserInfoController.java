package com.njm.yaho.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.njm.yaho.mapper.oracle.user.UserMapperOC;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalUserInfoController {
	@Autowired
	private UserMapperOC userMapperOC;
	
	@ModelAttribute
    public void addUserInfoToModel(HttpSession session, Model model) {
        String USER_ID = (String) session.getAttribute("USER_ID");

        if (USER_ID != null) {
        	int USER_PERMISSION = userMapperOC.checkUserRole(USER_ID);
			String USER_PROFILE_IMG = userMapperOC.getUserProfileImg(USER_ID);
            
			session.setAttribute("USER_PROFILE_IMG", USER_PROFILE_IMG);
			session.setAttribute("USER_PERMISSION", USER_PERMISSION);
			
            model.addAttribute("USER_ID", USER_ID);
            model.addAttribute("USER_PROFILE_IMG", USER_PROFILE_IMG);
            model.addAttribute("USER_PERMISSION", USER_PERMISSION);
        } else {
            model.addAttribute("USER_PERMISSION", 1); // 비회원 권한
        }
    }
}
