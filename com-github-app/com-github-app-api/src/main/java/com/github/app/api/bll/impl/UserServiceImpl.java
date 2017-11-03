package com.github.app.api.bll.impl;

import com.github.app.api.bll.UserService;
import com.github.app.api.dal.domain.UserExample;
import com.github.app.api.dal.dao.UserMapper;
import com.github.app.api.dal.domain.User;
import com.github.iot.utils.MD5Utils;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean auth(String account, String password) {
        UserExample userExample = new UserExample();
        userExample.or().andAccountEqualTo(account);
        List<User> list = userMapper.selectByExample(userExample);

        if(list == null || list.size() != 1)
            return false;

        if(!MD5Utils.validateMd5WithSalt(password, list.get(0).getPassword())) {
            return false;
        }

        return true;
    }

    @Override
    public JsonObject selectUserInfoByAccount(String account) {
        UserExample userExample = new UserExample();
        userExample.or().andAccountEqualTo(account);
        List<User> list = userMapper.selectByExampleWithRole(userExample);

        if(list == null || list.size() != 1)
            return null;

        JsonObject jsonObject = JsonObject.mapFrom(list.get(0));
        jsonObject.remove("password");
        return jsonObject;
    }

}
