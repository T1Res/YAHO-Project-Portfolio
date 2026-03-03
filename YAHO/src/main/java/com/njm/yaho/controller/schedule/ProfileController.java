package com.njm.yaho.controller.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.njm.yaho.controller.info.RateControllerTest;
import com.njm.yaho.service.schedule.ImageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

	private static final Logger log = LoggerFactory.getLogger(RateControllerTest.class);

	
    @Autowired
    private ImageService imageService;	

    @GetMapping("/profile")
    public String getProfile(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("USER_ID");
        String profileImagePath = getProfileImagePath(userId);
        log.info("이미지 경로 확인 :"+profileImagePath);
        model.addAttribute("profileImagePath", profileImagePath);
        return "profile";
    }

    private String getProfileImagePath(String userId) {
        if (userId == null) {
            return "/IMG/kibon_image.jpg";
        }
        String userProfileImg = imageService.getUserProfileImgById(userId);
        return (userProfileImg != null && !userProfileImg.isEmpty()) ? userProfileImg : "/IMG/kibon_image.jpg";
    }
}