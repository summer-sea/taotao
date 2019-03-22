package com.taotao.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserControlle {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {
		
		TaotaoResult result = null;
		
		//参数有效性校验
		if (StringUtils.isBlank(param)) {
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if (type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		}
		if (type != 1 && type != 2 && type != 3 ) {
			result = TaotaoResult.build(400, "校验内容类型错误");
		}
		//校验出错
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result; 
			}
		}
		//调用服务
		try {
			result = userService.checkData(param, type);
			
		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result; 
		}
	}
	
	//加上method为post 如果不加就post和get都支持
	@RequestMapping(value ="/register",method =RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user){
		try {
			TaotaoResult result = userService.createUser(user);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username,String password){
		try {
			TaotaoResult result =userService.userLogin(username, password);
			return result;
		} catch (Exception e) {
			
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
	}
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		TaotaoResult result =null;
		try {
			result=userService.getUserByToken(token);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//判断是是否为jsonp调用
		if(StringUtils.isBlank(callback)){
			return result;
		}else{
			MappingJacksonValue mappingJacksonValue =new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
	}
	/**
	 * 用户退出
	 */
	@RequestMapping("/tuichu/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token){
		userService.tcUser(token);
		return TaotaoResult.ok();
	}
}



