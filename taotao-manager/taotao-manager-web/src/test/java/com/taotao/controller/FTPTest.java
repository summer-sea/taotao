package com.taotao.controller;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import com.taotao.common.utils.FtpUtil;

public class FTPTest {
	//@Test
	public void testFtoClient() throws Exception{
		//创建一个ftpclient对象
		FTPClient ftpClient = new FTPClient();		
		//创建ftp链接
		ftpClient.connect("192.168.85.129",21);	
		//登录ftp服务器，使用用户名和密码
		ftpClient.login("ftpuser", "centos123456");
		//上传文件
		FileInputStream inputStream = new FileInputStream(new File("G:\\image\\3.jpg"));
		//设置为被动模式，解决上传上图片是0kb的问题
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		ftpClient.storeFile("hello6.jpg", inputStream);	
		//关闭链接
		ftpClient.logout();
		
	}
	//@Test
	public void testFtpUtils()throws Exception{
		FileInputStream inputStream = new FileInputStream(new File("G:\\image\\2.jpg"));
		FtpUtil.uploadFile("192.168.85.129", 21, "ftpuser", "centos123456", "/home/ftpuser/www/images",
				"2015/09/04", "hello8.jpg", inputStream);
		
	}

}
