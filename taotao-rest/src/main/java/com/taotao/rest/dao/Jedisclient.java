package com.taotao.rest.dao;

public interface Jedisclient {
	
	String get(String key);
	String set(String key,String value);
	String hget(String hkey,String key);
	long hset(String hkey,String key,String value);
	long incr(String key);
	//设置有效时间
	long expire(String key,int second);
	//查看有效时间
	long ttl(String key);
	//删除，同步缓存时候应用
	long del(String key);
	long hdel(String hkey,String key);

}
