package com.gsdai.backend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.UserMapper;
import com.gsdai.backend.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword, String avatarStr) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("errorMessage", "username cannot be null");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("errorMessage", "username cannot be null");
            return map;
        }

        if (username.length() > 100) {
            map.put("errorMessage", "the length of username could not be longer than 100");
            return map;
        }

        if (password == null || password.length() == 0) {
            map.put("errorMessage", "password cannot be null");
            return map;
        }

        if (password.length() > 100) {
            map.put("errorMessage", "the length of password could not be longer than 100");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("errorMessage", "password dose not match confirmed password");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("errorMessage", "username has existed");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword, avatarStr, 0);
        userMapper.insert(user);

        map.put("errorMessage", "success");
        return map;
    }
}
