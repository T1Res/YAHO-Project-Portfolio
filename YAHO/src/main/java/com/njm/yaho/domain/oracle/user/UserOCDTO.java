package com.njm.yaho.domain.oracle.user;

import java.util.Date;

@lombok.Data
public class UserOCDTO {
	private String userId;
    private String userPassword;
    private String userNickname;
    private String userEmail;
    private String userBio;
    private String userProfileImg;
    private Date userCreatedDate;
    private int userPermission;
    private int userGender;
}
