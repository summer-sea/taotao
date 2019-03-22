package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
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

	@Override
	public TaotaoResult createItem(TbItem item,String desc ,String itemParam)throws Exception{
		//需要生成商品Id
		Long itemId =IDUtils.genItemId();
		item.setId(itemId);
		//商品的转态1 正常，2下架，3删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//插入到数据库
		itemMapper.insert(item);
		//添加商品描述信息
		TaotaoResult result=insertItemDesc(itemId, desc);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		 result =insertItemParamTem(itemId, itemParam);
		 if (result.getStatus() != 200) {
				throw new Exception();
			}
		 return TaotaoResult.ok();
	}
	//添加商品描述
	private TaotaoResult insertItemDesc(Long itemId,String desc){
		TbItemDesc itemDesc =new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		
		return TaotaoResult.ok();
	}
/*
 * 
 * 添加商品描述
 * */
	private TaotaoResult insertItemParamTem(Long itemId,String itemParam)
	{
		//创建一个pojo
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		//向表中插入数据
		itemParamItemMapper.insert(itemParamItem);
		
		return TaotaoResult.ok();
		 
	}
	
	
}
