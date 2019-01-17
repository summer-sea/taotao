package com.taotao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public TbItem getItemById(long itemId) {
		
		TbItemExample example = new TbItemExample();
		com.taotao.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		 List<TbItem> list = itemMapper.selectByExample(example );
		if(list !=null && list.size() >0){
			TbItem item =list.get(0);
			return item;
		}
		 // TODO Auto-generated method stub
		return null;
	}

	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		TbItemExample example =new TbItemExample();
		//分页处理
		PageHelper.startPage(page, rows);
		List<TbItem> list =itemMapper.selectByExample(example);
		EUDataGridResult result =new EUDataGridResult();
		result.setRows(list);
		//取记录总数
		PageInfo<TbItem> pageInfo =new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}

}
