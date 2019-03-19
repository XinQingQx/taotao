package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户注册接口
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper mapper;

    @Autowired
    private JedisClient client;

    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    /**
     * 检查数据是否可用
     *
     * @param param 校验的数据
     * @param type  可选参数1、2、3分别代表username、phone、email
     * @return
     */
    @Override
    public TaotaoResult checkUserData(String param, Integer type) {

        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        if (type==1){
            criteria.andUsernameEqualTo(param);//判断用户名是否可用
        }else if (type == 2) {
            criteria.andPhoneEqualTo(param);//判断电话是否可用
        }else if (type == 3){
            criteria.andEmailEqualTo(param);//判断邮箱是否可用
        }else {
            return TaotaoResult.build(400,"参数非法");
        }

        List<TbUser> tbUsers = mapper.selectByExample(userExample);
        if (tbUsers != null && tbUsers.size() > 0){
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public TaotaoResult register(TbUser user) {

        //检查数据有效性
        System.out.println(user.getUsername());
        if (StringUtils.isBlank(user.getUsername())){
            return TaotaoResult.build(400,"用户名不能为空!!!");
        }
        TaotaoResult taotaoResult = checkUserData(user.getUsername(), 1);
        if (!(Boolean) taotaoResult.getData()){
            return TaotaoResult.build(400,"用户名不能重复");
        }


        if (StringUtils.isBlank(user.getPassword())){
            return TaotaoResult.build(400,"密码不能为空！");
        }
        if(StringUtils.isNotBlank(user.getPhone())){
            //如果电话不为空，那么接着判断是否重复，电话是不能重复的
            taotaoResult = checkUserData(user.getPhone(), 2);
            if(!(Boolean)taotaoResult.getData()){
                return TaotaoResult.build(400, "电话不能重复！");
            }
        }
        if(StringUtils.isNotBlank(user.getEmail())){
            //如果邮箱不为空，那么接着判断是否重复，邮箱也是不能重复的
            taotaoResult = checkUserData(user.getEmail(), 3);
            if(!(Boolean)taotaoResult.getData()){
                return TaotaoResult.build(400, "邮箱不能重复！");
            }
        }

        //填充属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //密码要进行Md5加密，我们不用添加额外的jar包，只需要使用Spring自带的包就可以了
        String md5Str = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Str);
        //添加
        mapper.insert(user);
        return TaotaoResult.ok();

    }
}
