package com.taotao.rest.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.rest.dao.Jedisclient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * jedis 单机版本实现类
 * @author x
 *
 */
public class JedisClientSingle implements Jedisclient {
	@Autowired
	private JedisPool jedisPool;

	@Override
	public String get(String key) {

		Jedis jedis =jedisPool.getResource();
		String string = jedis.get(key);
		jedis.close();
		return string;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis =jedisPool.getResource();
		 String string =jedis.set(key, value);
		 jedis.close();
		return string;
	}

	@Override
	public String hget(String hkey, String key) {
		Jedis jedis =jedisPool.getResource();
		//hkey：key-vlue
		String string =jedis.hget(hkey,key);
		jedis.close();
		return string;
	}

	@Override
	public long hset(String hkey, String key, String value) {
		Jedis jedis =jedisPool.getResource();
		long result = jedis.hset(hkey, key, value);
		jedis.close();
		return result;
	}

	@Override
	public long incr(String key) {
		Jedis jedis =jedisPool.getResource();
		long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public long expire(String key, int second) {
		Jedis jedis =jedisPool.getResource();
		long result = jedis.expire(key, second);
		jedis.close();
		return result;
	}

	@Override
	public long ttl(String key) {
		Jedis jedis =jedisPool.getResource();
		long result = jedis.ttl(key);
		jedis.close();
		return result;
		
	}
/**
 * 单机版本
 * del删除key
 * 做同步缓存时候可以直接使用
 */
	@Override
	public long del(String key) {
		Jedis jedis =jedisPool.getResource();
		long result = jedis.del(key);
		jedis.close();
		return result;
	}

@Override
public long hdel(String hkey, String key) {

	Jedis jedis =jedisPool.getResource();
	long result = jedis.hdel(hkey, key);
	jedis.close();
	return result;
}

}
