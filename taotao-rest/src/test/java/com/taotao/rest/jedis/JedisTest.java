package com.taotao.rest.jedis;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	// 单机版测试
	@Test
	public void testJedisSingle() {
		// 创建jedis的对象
		Jedis jedis = new Jedis("192.168.1.102", 6379);
		// 直接调用jedis对象的方法，方法名称和redis的命令一致
		jedis.set("key1", "jedis test");
		String string = jedis.get("key1");
		System.out.println("String: " + string);
		// 关闭jedis
		jedis.close();

	}

	/**
	 * 使用连接池
	 * 
	 */
	@Test
	public void testJedisPool() {
		// 创建一个jedispool 连接池
		JedisPool pool = new JedisPool("192.168.1.102", 6379);
		// 从连接池获取jedis对象
		Jedis jedis = pool.getResource();
		String string = jedis.get("key1");
		System.out.println("StringPool: " + string);
		// 每次使用完要关闭，否则连接池就会爆
		jedis.close();
	}

	/**
	 * 集群版测试
	 */

	@Test
	public void testJedisCluster() {
		// 创建节点集合
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.1.102", 7001));
		nodes.add(new HostAndPort("192.168.1.102", 7002));
		nodes.add(new HostAndPort("192.168.1.102", 7003));
		nodes.add(new HostAndPort("192.168.1.102", 7004));
		nodes.add(new HostAndPort("192.168.1.102", 7005));
		nodes.add(new HostAndPort("192.168.1.102", 7006));
		// 创建一个集群对象 注意集群不需要创建连接池，jediscluster自带连接池
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("key1", "1000");
		String string = cluster.get("key1");
		System.out.println("stringCluster: " + string);
		cluster.close();
	}

	/**
	 * 测试spring 和redis的整合 单机版
	 */
	@Test
	public void testSpringJedisSingle() {
		// 加载配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");

		JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
		Jedis jedis = pool.getResource();
		String string = jedis.get("key1");
		System.out.println(string);
		jedis.close();
		pool.close();
	}

	/**
	 * 测试spring 和redis的整合 集群版测试
	 * 
	 */
	@Test
	public void testSpringJedisCluster() {
		// 加载配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClient");
		 String string=jedisCluster.get("key1");
		 System.out.println(string);
		 jedisCluster.close();
	}
}
