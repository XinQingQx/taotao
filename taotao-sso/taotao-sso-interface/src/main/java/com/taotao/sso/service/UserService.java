package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * 用户注册接口
 */
public interface UserService {

    /**
     * 检查数据是否可用
     * @param param 校验的数据
     * @param type 可选参数1、2、3分别代表username、phone、email
     * @return
     */
    TaotaoResult checkUserData(String param, Integer type);

    /**
     * 用户注册
     * @param user
     * @return
     */
    TaotaoResult register(TbUser user);

}
