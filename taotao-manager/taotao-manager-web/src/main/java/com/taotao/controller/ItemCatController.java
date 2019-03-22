package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.service.ItemCatService;

/*
 * 商品分类管理
 * */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService catService;
	
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getCatList(@RequestParam(value ="id",defaultValue="0")Long parentId){
		List<EUTreeNode> list =catService.getCatList(parentId);
		
		return list;
	}

}
