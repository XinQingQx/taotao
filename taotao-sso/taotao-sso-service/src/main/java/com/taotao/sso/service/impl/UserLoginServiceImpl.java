package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private TbUserMapper mapper;

	@Autowired
	private JedisClient client;

	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public TaotaoResult login(String username, String password) {

		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = mapper.selectByExample(example);
		if(list == null || list.size() == 0){
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确！");
		}
		TbUser user = list.get(0);
		//密码要进行md5加密后再校验
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确！");
		}
		//生成token，使用uuid
		String token = UUID.randomUUID().toString();
		//把用户信息保存到redis当中，key就是token，value就是用户信息
		//我们在redis中存放用户信息不要存密码，因为这样太危险了，因此我们先把密码置空
		user.setPassword(null);
		client.set(USER_SESSION+":"+token, JsonUtils.objectToJson(user));
		//设置过期时间
		client.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//返回登录成功，要记得带回token信息
		return TaotaoResult.ok(token);
	}

	/**
	 * 根据token获取用户的信息
	 *
	 * @param token
	 * @return TaotaoResult 应该包含用户的信息
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {

		//获取登陆信息（redis中）
		String json = client.get(USER_SESSION + ":" + token);

		if (StringUtils.isNotBlank(json)){

			//转换成tbUser
			TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);

			//重新设置过期时间
			client.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
			return TaotaoResult.ok(tbUser);

		}

		return TaotaoResult.build(400,"用户已过期，请重新登陆");
	}

}
