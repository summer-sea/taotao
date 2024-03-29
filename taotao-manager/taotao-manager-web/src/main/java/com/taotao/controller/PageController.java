package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 
 * 页面跳转Controller
 * 
 * */

@Controller
public class PageController {
	
	/**
	 * 打开首页
	 * 
	 * */
	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
		
	}
	/**
	 * 展示其它页面 
	 * */
	@RequestMapping("/{page}")
	public String showpage(@PathVariable String page){
		return page;
	}

}
