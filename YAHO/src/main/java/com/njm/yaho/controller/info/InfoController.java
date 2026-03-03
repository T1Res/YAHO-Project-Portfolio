package com.njm.yaho.controller.info;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Info")
public class InfoController {
	
	@GetMapping("/teamInfo")
    public String teamInfo() {
        return "Info/teamInfo";
    }
	@GetMapping("/useInfo")
    public String useInfo() {
        return "Info/useInfo";
    }
	@GetMapping("/policy")
    public String policyInfo() {
        return "Info/policy";
    }
	@GetMapping("/useTerms")
    public String useTerms() {
        return "Info/useTerms";
    }
}
