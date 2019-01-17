package com.taotao.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	
	//@Test
	public void testPageHelper(){
		//创建一个spring容器
		ApplicationContext applicationContext =new 
				ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//从spring容器中获取mapper代理对象
		TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		//指向查询并分页
		TbItemExample example =new TbItemExample();
		
		PageHelper.startPage(1, 10);
		List<TbItem> list = mapper.selectByExample(example);
		for(TbItem item:list){
			System.out.println(":"+item);
		}
		//取分页信息
		PageInfo<TbItem> pageInfo=  new PageInfo<>(list); 
		long total = pageInfo.getTotal();
		System.out.println("total:"+total);
		
		
	}

}
