package com.njm.yaho.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njm.yaho.domain.oracle.user.UserDTO;
import com.njm.yaho.service.user.UserService;
import com.njm.yaho.util.PassUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 자동주입-오토와이드 안써도됨
public class UserController {
	// 로거 출력
	private static final Logger log = LoggerFactory.getLogger("UserController.class");

	private final UserService service;

	// 회원가입
	@GetMapping("User/User_Insert")
	public String UserInsert() {
		return "User/User_Insert";
	}

	@PostMapping("User/User_Insert")
	public String userInsertPro(@ModelAttribute UserDTO dto,
			@RequestParam(value = "profile", required = false) MultipartFile profile,
			RedirectAttributes redirectAttributes) {

		

		// ✅ 비밀번호 암호화 (Spring Security 없이)
	    String encryptedPassword = PassUtil.hashPassword(dto.getUSER_PASSWORD());
	    dto.setUSER_PASSWORD(encryptedPassword);
	    
	    dto.setUSER_PERMISSION(1);
	    
		// 프로필 이미지 처리 (선택 구현)
		if (profile != null && !profile.isEmpty()) {
			String savedPath = service.saveProfileImage(profile); // 따로 구현
			dto.setUSER_PROFILE_IMG(savedPath);
		}else {
            dto.setUSER_PROFILE_IMG("/IMG/kibon_image.jpg"); // 기본 이미지 설정
        }

		// DB 저장
		int result = service.UserInsert(dto);

		if (result > 0) {
			redirectAttributes.addFlashAttribute("msg", "회원가입 성공!");
			return "redirect:/User/User_Login";
		} else {
			redirectAttributes.addFlashAttribute("msg", "회원가입 실패");
			return "redirect:/User/User_Insert";
		}
	}

	// 로그인
	@GetMapping("User/User_Login")
	public String UserLogin(Model model) {
		if (!model.containsAttribute("error")) {
            model.addAttribute("error", "");
        }
		return "User/User_Login";
	}

	@PostMapping("User/User_Login")
	public String UserLoginPro(@ModelAttribute UserDTO dto, RedirectAttributes redirectAttributes, HttpSession session) {
		UserDTO loggedInUser = service.UserLogin(dto.getUSER_ID(), dto.getUSER_PASSWORD());
		if (loggedInUser != null) {
			session.setAttribute("USER_ID", loggedInUser.getUSER_ID());
			session.setAttribute("user", loggedInUser);
			session.setAttribute("playAudio", true);
			return "redirect:/Main"; // 로그인 후 이동할 페이지
		} else {
			redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
			return "redirect:/User/User_Login";
		}
	}
	// 중복검사
	@PostMapping("/User/User_IdCheck")
	@ResponseBody
	public int UserIdCheck(@RequestParam("USER_ID") String USER_ID) {
	    UserDTO user = service.UserIdCheck(USER_ID);
	    return (user == null) ? 0 : 1; // 0: 사용 가능, 1: 중복
	}
	@PostMapping("/User/User_EmailCheck")
	@ResponseBody
	public int UserEmailCheck(@RequestParam("USER_EMAIL") String USER_EMAIL) {
	    UserDTO user = service.UserEmailCheck(USER_EMAIL);
	    return (user == null) ? 0 : 1;
	}
	@GetMapping("/User/User_EmailCheck") //이메일중복검사페이지
	public String showEmailCheckForm() {
	    return "/User/User_EmailCheck"; // src/main/resources/templates/email_check.html
	}
	@GetMapping("/User/User_InsertForm") //이메일중복검사
	public String UserInsertForm(@RequestParam("USER_EMAIL") String USER_EMAIL, Model model) {
	    model.addAttribute("USER_EMAIL", USER_EMAIL);
	    return "/User/User_Insert"; // 위 HTML 파일에 해당
	}
	// 회원수정
	@GetMapping("/User/User_Modify")
	public String UserModify(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		 String USER_ID = (String) session.getAttribute("USER_ID");

		    if (USER_ID == null) {
		        redirectAttributes.addFlashAttribute("msg", "로그인이 필요합니다.");
		        return "redirect:/User/User_Login";
		    }

		    UserDTO dto = service.UserSearch(USER_ID);
		    if (dto == null) {
		        redirectAttributes.addFlashAttribute("msg", "회원 정보를 불러올 수 없습니다.");
		        return "redirect:/";
		    }

		    String[] emailParts = dto.getUSER_EMAIL().split("@");
		    model.addAttribute("email1", emailParts[0]);
		    model.addAttribute("email2", emailParts.length > 1 ? emailParts[1] : "");
		    model.addAttribute("dto", dto);
		    model.addAttribute("timestamp", System.currentTimeMillis());
		    return "User/User_Modify"; // 명시적으로 반환
	}
	//회원수정처리
	@PostMapping("/User/User_Modify")
	public String modifyUser(@ModelAttribute("user") UserDTO dto,
			@RequestParam(value = "USER_PROFILE_PATH", required = false) MultipartFile profileImg, HttpSession session,
			RedirectAttributes redirectAttributes) {

		String USER_ID = (String) session.getAttribute("USER_ID");
		
	    if (USER_ID == null) {
	        redirectAttributes.addFlashAttribute("msg", "로그인 세션이 만료되었습니다.");
	        return "redirect:/User/User_Login";
	    }

	    dto.setUSER_ID(USER_ID);
	    
	    UserDTO origin =service.UserSearch(USER_ID);
	    
	    // 비밀번호 변경 시 암호화
	    if (dto.getUSER_PASSWORD() != null && !dto.getUSER_PASSWORD().isBlank()) {
	        String encryptedPassword = PassUtil.hashPassword(dto.getUSER_PASSWORD());
	        dto.setUSER_PASSWORD(encryptedPassword);
	    } else {
	    	dto.setUSER_PASSWORD(null); // null로 하면 MyBatis <if> 조건문 통해 UPDATE 안 함
	    }

		// 프로필 이미지 처리 (선택 사항)
		if (profileImg != null && !profileImg.isEmpty()) {
			String profilePath = service.saveProfileImage(profileImg); // 실제 구현 필요
			dto.setUSER_PROFILE_IMG(profilePath);
		}else {
			dto.setUSER_PROFILE_IMG(origin.getUSER_PROFILE_IMG());
		}
		
		// 수정 처리
		service.UserModify(dto);

		// 성공 메시지 전달
		redirectAttributes.addFlashAttribute("msg", "회원정보가 수정되었습니다.");

		return "redirect:/";
	}
	//프로필보기
	@GetMapping("/User/User_Profile")
	public String UserProfile(Model model,HttpSession session) {
		String USER_ID = (String) session.getAttribute("USER_ID");
	    if (USER_ID == null) return "redirect:/User/User_Login";

	    UserDTO dto = service.UserProfile(USER_ID);
	    model.addAttribute("dto", dto);


	    return "User/User_Profile";
	}
	
	// 회원탈퇴
	@GetMapping("/User/User_Delete")
	public String UserDelete() {
		
		return "User/User_Delete";
	}
	@PostMapping("/User/User_Delete")
	public ResponseEntity<String> UserDeletePro(HttpSession session,@RequestParam("password") String password) {
		String USER_ID = (String) session.getAttribute("USER_ID");
		UserDTO dto = service.UserSearch(USER_ID);
		
		// 비밀번호 일치 확인
		if (dto == null || !PassUtil.checkPassword(password, dto.getUSER_PASSWORD())) {
	        return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다."); // 비밀번호 불일치
	    }

	    service.UserDelete(USER_ID);
	    session.invalidate(); // 탈퇴 후 로그아웃
	    
		return ResponseEntity.ok("회원탈퇴에 성공했습니다.");
	}
	
	// 로그아웃
	@GetMapping("/User/User_Logout")
	public String UserLogout(HttpSession session,RedirectAttributes redirectAttributes) {
		session.invalidate(); // 세션 전체 삭제
		redirectAttributes.addFlashAttribute("playAudio", true);
	    return "redirect:/";  // 메인으로
  }
}