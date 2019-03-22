package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转页面
 * @author x
 *
 */
@Controller
@RequestMapping("/Page")
public class PageController {
	
	@RequestMapping("/register")
	public String showRegister(){
		
		return "register";
	}
	
	

}
