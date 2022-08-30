package com.gsdai.backend.service.impl.rank;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.UserMapper;
import com.gsdai.backend.service.rank.GetRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRankServiceImpl implements GetRankService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage = new Page<>(page, 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("marks");
        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for (User user : users)
            user.setPassword("");
        resp.put("users", users);
        resp.put("usersCount", userMapper.selectCount(null));
        return resp;
    }
}
