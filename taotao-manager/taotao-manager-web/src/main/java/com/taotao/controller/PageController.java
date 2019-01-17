package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 
 * ҳ����תController
 * 
 * */

@Controller
public class PageController {
	
	/**
	 * ����ҳ
	 * 
	 * */
	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
		
	}
	/**
	 * չʾ����ҳ�� 
	 * */
	@RequestMapping("/{page}")
	public String showpage(@PathVariable String page){
		return page;
	}

}
