package com.njm.yaho.service.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njm.yaho.domain.oracle.util.EmailAuthOCDTO;
import com.njm.yaho.mapper.oracle.user.UserMapperOC;
import com.njm.yaho.mapper.oracle.util.EmailAuthMapperOC;
import com.njm.yaho.util.AuthCodeGenerator;

import jakarta.servlet.http.HttpSession;

@Service
public class EmailAuthServiceImpl implements EmailAuthService {
	@Autowired
    private EmailAuthMapperOC emailAuthMapper;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private UserMapperOC userMapper;
    
    @Override
	public boolean emailExistsInUser(String email) {
    	return userMapper.countByEmail(email) > 0;
	}
    
    @Override
    public void sendAuthCode(String email) {
        EmailAuthOCDTO existing = emailAuthMapper.findByEmail(email);

        if (existing != null && existing.getTryCount() >= 5) {
            throw new IllegalStateException("너무 많은 시도를 했습니다.");
        }

        String code = AuthCodeGenerator.generateCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plusMinutes(10);

        EmailAuthOCDTO dto = new EmailAuthOCDTO();
        dto.setEmail(email);
        dto.setAuthCode(code);
        dto.setExpireTime(expire);
        dto.setSentTime(now);

        if (existing == null) {
            emailAuthMapper.insertEmailAuth(dto);
        } else {
            emailAuthMapper.updateAuthCodeAndIncrementTryCount(email,code,expire,now); // 🔄 tryCount 1 증가 포함
        }

        mailService.sendAuthEmail(email, code);
    }

	@Override
	public boolean verifyCode(String email, String inputCode, HttpSession session) {
		EmailAuthOCDTO dto = emailAuthMapper.findByEmailAndCode(email, inputCode);

	    if (dto == null) {
	        throw new IllegalArgumentException("인증코드가 일치하지 않습니다.");
	    }

	    if ("Y".equals(dto.getIsVerified())) {
	        throw new IllegalStateException("이미 인증이 완료된 이메일입니다.");
	    }

	    if (dto.getExpireTime().isBefore(LocalDateTime.now())) {
	        throw new IllegalStateException("인증코드가 만료되었습니다.");
	    }

	    emailAuthMapper.markEmailAsVerified(email);

	    // 인증 성공 → 세션 저장
	    session.setAttribute("verifiedEmail", email);
	    return true;
	}

	@Override
	public void deleteByEmail(String email) {
		 emailAuthMapper.deleteByEmail(email);
	}

}
