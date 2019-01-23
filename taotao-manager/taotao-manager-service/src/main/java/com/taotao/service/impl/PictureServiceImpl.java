package com.taotao.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;

/**
 * ͼƬ�ϴ�����
 * <p>Title: PictureServiceImpl</p>
 * 
 * 
 * @author	
 * @date	
 * @version 1.0
 */
@Service
public class PictureServiceImpl implements PictureService {
	
	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	@Override
	public Map uploadPicture(MultipartFile uploadFile) {
		Map resultMap = new HashMap<>();
		try {
			//����һ���µ��ļ���
			//ȡԭʼ�ļ���
			String oldName = uploadFile.getOriginalFilename();
			//�������ļ���
			//UUID.randomUUID();
			String newName = IDUtils.genImageName();
			newName = newName + oldName.substring(oldName.lastIndexOf("."));
			//ͼƬ�ϴ�
			String imagePath = new DateTime().toString("/yyyy/MM/dd");
			boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, 
					FTP_BASE_PATH, imagePath, newName, uploadFile.getInputStream());
			//���ؽ��
			if(!result) {
				resultMap.put("error", 1);
				resultMap.put("message", "�ļ��ϴ�ʧ��");
				return resultMap;
			}
			resultMap.put("error", 0);
			resultMap.put("url", IMAGE_BASE_URL + imagePath + "/" + newName);
			return resultMap;
			
		} catch (Exception e) {
			resultMap.put("error", 1);
			resultMap.put("message", "�ļ��ϴ������쳣");
			return resultMap;
		}
	}

}
