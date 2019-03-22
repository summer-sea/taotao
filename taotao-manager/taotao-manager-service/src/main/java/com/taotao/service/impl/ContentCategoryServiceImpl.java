package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;
//���ݷ������

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {
		//����parentid��ѯ�ڵ��б�
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//ִ�в�ѯ
		List<TbContentCategory> list =contentCategoryMapper.selectByExample(example);
		List<EUTreeNode> resultList =new ArrayList<>();
		for(TbContentCategory tbContentCategory :list){
			EUTreeNode node = new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			resultList.add(node);
			
		}
		return resultList;

	}

	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		//����һ��pojo
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		//״̬����ѡֵ��1���� 2 ɾ��
		contentCategory.setStatus(1);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//��Ӽ�¼
		contentCategoryMapper.insert(contentCategory);
		//�鿴���ڵ��isParent�� �Ƿ�Ϊtrue���������true���ĳ�true
		TbContentCategory parentCat=contentCategoryMapper.selectByPrimaryKey(parentId);
		//�ж��Ƿ�Ϊtrue
		if(!parentCat.getIsParent()){
			parentCat.setIsParent(true);
			//���¸��ڵ�
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		
		return TaotaoResult.ok(contentCategory);
	}

}
