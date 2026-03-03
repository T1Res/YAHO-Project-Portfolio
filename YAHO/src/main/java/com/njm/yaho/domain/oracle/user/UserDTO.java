package com.njm.yaho.domain.oracle.user;

import lombok.Data;

@Data
public class UserDTO {
	private String USER_ID;
	private String USER_PASSWORD;
	private String USER_NICKNAME;
	private String USER_EMAIL;
	private String USER_BIO;
	private String USER_PROFILE_IMG;
	private int USER_GENDER;
	private String USER_CREATED_DATE;
	private int USER_PERMISSION;
}
