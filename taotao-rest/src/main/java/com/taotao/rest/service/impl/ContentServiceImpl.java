package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.Jedisclient;
import com.taotao.rest.service.ContentService;

/**
 * 
 * 内容管理Service
 * 
 * @author x
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	// 在文件外定义的properties 常量
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;

	@Autowired
	private Jedisclient jedisClient;

	@Override
	public List<TbContent> getContentList(long contentCid) {
		// 从缓存中取注意：缓存不能影响业务逻辑
		try {
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCid + "");
			if (!StringUtils.isBlank(result)) {
				// 把字符串转换成list
				List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据查询内容分类查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCid);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		// 向缓存中添加内容
		try {
			// 将内容转换成字符串
			String cacheString = JsonUtils.objectToJson(list);
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY, contentCid + "", cacheString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
