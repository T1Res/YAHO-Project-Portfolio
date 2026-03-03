package com.njm.yaho.service.schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njm.yaho.mapper.oracle.schedule.ImageMapper;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public String getUserProfileImgById(String userId) {
        return imageMapper.selectUserProfileImgById(userId);
    }
}