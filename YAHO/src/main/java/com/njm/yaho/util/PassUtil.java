package com.njm.yaho.util;

import org.mindrot.jbcrypt.BCrypt;

public class PassUtil {
	// 비밀번호 암호화
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 비밀번호 일치 확인
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
