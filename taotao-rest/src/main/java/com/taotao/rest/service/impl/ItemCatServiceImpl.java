package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

/*
 * 分类服务
 * */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public CatResult getItemCatList() {
		CatResult catResult = new CatResult();
		catResult.setData(getCatList(0));
		return catResult;

	}

	private List<?> getCatList(long parentId) {
		// 创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询 返回值list
		List<TbItemCat> list = itemCatMapper.selectByExample(example);

		List resultlist = new ArrayList<>();
		int count =0;
		for (TbItemCat tbItemCat : list) {
			if (tbItemCat.getIsParent()) {
				CatNode catNode = new CatNode();
				if (parentId == 0) {
					catNode.setName(
							"<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
				} else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
				catNode.setItem(getCatList(tbItemCat.getId()));
				resultlist.add(catNode);
				//判断到14个就不取了 因为页面会乱，
				count++;
				if(parentId == 0 &&count >=14){
					break;
				}
			} else {
				// 如果是叶子节点
				resultlist.add("/products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());

			}
		}
		return resultlist;
	}

}
