package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.service.PictureService;

@Controller
public class PictureController {
	
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping("pic/upload")
	@ResponseBody
	public String pictureUpload(MultipartFile uploadFile){
			Map	result = pictureService.uploadPicture(uploadFile);
			//为了保证兼容性要把result转换成json字符串
			String json =JsonUtils.objectToJson(result);
			System.out.println("result:"+result);
		return json;
	}

}
