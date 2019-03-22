package com.taotao.service.impl;

import java.util.Date;

import javax.swing.text.html.parser.ContentModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

/**
 * 内容管理
 * @author x
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;
	
	@Override
	public TaotaoResult insertContent(TbContent content) {
		
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//添加缓存同步逻辑
		try {
			//rest风格的服务，实际上是用httpClient调用http的网址类型
			//http://192.168.25.136:8080/rest/cache/sync/content/
			//REST_CONTENT_SYNC_URL=/cache/sync/content/
			HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return TaotaoResult.ok();
	}

}
