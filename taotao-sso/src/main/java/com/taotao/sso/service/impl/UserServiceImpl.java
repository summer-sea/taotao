package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.impl.JedisClientSingle;
import com.taotao.sso.service.UserService;

/**
 * 
 * 用户管理Service
 * 
 * @author x
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMpper;

	@Autowired
	private JedisClientSingle jedisClient;

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;

	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	@Override
	public TaotaoResult checkData(String content, Integer type) {
		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 对数据进行校验
		if (1 == type) {
			// 用户名校验
			criteria.andUsernameEqualTo(content);
		} else if (2 == type) {
			criteria.andPhoneEqualTo(content);

		} else {
			criteria.andEmailEqualTo(content);

		}
		// 执行查询
		List<TbUser> list = userMpper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);

		}
		return TaotaoResult.ok(false);
	}

	@Override
	public TaotaoResult createUser(TbUser user) {
		user.setUpdated(new Date());
		user.setCreated(new Date());
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		userMpper.insert(user);
		return TaotaoResult.ok();
	}

	/**
	 * 用户登录
	 */
	@Override
	public TaotaoResult userLogin(String username, String password) {
		// 比对用户名和密码
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMpper.selectByExample(example);
		// 如果没有此用户名提示为空
		if (null == list || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码错误");

		}
		TbUser user = list.get(0);
		if (!DigestUtils.md5DigestAsHex(user.getPassword().getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		// 生成token
		String token = UUID.randomUUID().toString();
		// 保存用户之前，把用户对象密码清空
		user.setPassword(null);
		// 把用户信息写入redis
		jedisClient.set(REDIS_USER_SESSION_KEY + "：" + token, JsonUtils.objectToJson(user));
		// 设置session 过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + "：" + token, SSO_SESSION_EXPIRE);

		// 返回token
		return TaotaoResult.ok(token);

	}

	/**
	 * 根据token取用户信息
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		String jspn= jedisClient.get(REDIS_USER_SESSION_KEY + "：" + token);
		if(StringUtils.isBlank(jspn)){
			TaotaoResult.build(400, "此session已经过期，请重新登录");
		}
		//更新过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + "：" + token, SSO_SESSION_EXPIRE);
		//返回用户信息
		return TaotaoResult.ok(jspn);
	}

	/**
	 * 用户退出
	 */
	@Override
	public TaotaoResult tcUser(String token) {
		jedisClient.del(REDIS_USER_SESSION_KEY + "：" + token);
		return TaotaoResult.ok();
	}

}
