package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId){
		TbItem tbItem = itemService.getItemById(itemId);	
		return tbItem;
	}
	
	@RequestMapping("item/list")
	@ResponseBody
	public EUDataGridResult getItemList(Integer page,Integer rows){
		//System.out.println("page:"+page+" rows:"+rows);
		EUDataGridResult result =new EUDataGridResult();
		result =itemService.getItemList(page, rows);
		
		return result;
	}
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createItem(TbItem item,String desc,String itemParams) throws Exception{
		TaotaoResult result=itemService.createItem(item,desc,itemParams);
		return result;
	}

}
